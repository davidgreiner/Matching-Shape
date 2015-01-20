package de.ultitech.matchingshape;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * This class is used to create and manage a Bluetooth connection.
 * This class was first designed to scan for and connect to Bluetooth devices by name, but this feature was scrapped
 * due to unpredictable behaviour when connecting to unpaired devices.
 * Because of that, this class requires the device to be pre-paired with the LED-Matrix before establishing the
 * connection. Once connected, this class can be used to send data to the matrix.
 *
 * @author Vincent Diener
 */
public class LEDMatrixBTConn {

    // The protocol version used.
    private static final  byte VERSION = (byte) 1;

    // Tag string for log messages.
    private static final String LED_MATRIX_BT_CONN = "LEDMatrixBTConn";

    // Bluetooth name of the device we connect to.
    private volatile String mRemoteBTDeviceName;

    // The Bluetooth adapter on this device.
    private volatile BluetoothAdapter mBluetoothAdapter;

    // The socket used to connect to the remote device.
    private volatile BluetoothSocket btSocket;

    // The data stream used to send data to the remote device.
    private volatile OutputStream outStream;

    // The data stream used to receive data from the remote device.
    private volatile InputStream inStream;

    // Main activity context.
    private volatile Context mContext;

    // MAC address of the of the Bluetooth device.
    private volatile String mAddress;

    // The maximum frames per second supported by the display
    private volatile int mMaxFPS;

    // The size of the display in LEDs in the X direction.
    private volatile int mXSize;

    // The size of the display in LEDs in the Y direction.
    private volatile int mYSize;

    // The color mode to use.
    private volatile int mColorMode;

    // The name of the App that will be making the connection. This will be logged by the server.
    private volatile String mAppName;

    /**
     * The constructor.
     *
     * @param context The context of the main activity.
     * @param remoteBTDeviceName The name of the remote Bluetooth device that we will scan for.
     */
    public LEDMatrixBTConn(Context context, String remoteBTDeviceName, int xSize, int ySize, int colorMode, String appName){
        mRemoteBTDeviceName = remoteBTDeviceName;
        mContext = context;
        mAddress = null;
        mXSize = xSize;
        mYSize = ySize;
        mAppName = appName;
        mColorMode = colorMode;
    }

    /**
     * Returns the name of the remote Bluetooth device.
     *
     * @return The name of the remote Bluetooth device.
     */
    public String getRemoteDeviceName() {
        return mRemoteBTDeviceName;
    }

    /**
     * Returns the maximum supported frames per second of the display.
     *
     * @return The maximum supported frames per second of the display.
     */
    public int getMaxFPS() {
        return mMaxFPS;
    }

    /**
     * Prepares the Bluetooth adapter for starting the device discovery.
     * This will check if Bluetooth is both available and enabled on this device and return true
     * if that's the case, false otherwise.
     *
     * @return True if Bluetooth can be used on this device, false otherwise.
     */
    public boolean prepare() {
        // Get the default Bluetooth adapter of this device.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if Bluetooth is available on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, "Bluetooth is not available on this device.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Check if Bluetooth is enabled on the device.
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(mContext, "Please enable your BT and re-run this program.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Bluetooth adapter is ready to be used. Return true.
        return true;
    }


    public boolean checkIfDeviceIsPaired() {
        // Check bonded (=paired) devices.
        for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()) {
            Log.d(LED_MATRIX_BT_CONN, "Checking bonded: " + device.getName());

            if (device.getName().equals(mRemoteBTDeviceName)) {
                mAddress = device.getAddress();
            }
        }
	        	
	    /*
	    * If mAddress is null, the device is not paired.
	    */
        if (mAddress == null) {
            Toast.makeText(mContext, "Device with name " + mRemoteBTDeviceName + " is not paired. Please pair first then restart the app.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    /**
     * Called by onPause() in the main activity.
     * Closes the connection to the remote device.
     */
    public void onPause() {
        this.closeSocket();
    }

    /**
     * Closes the connection to the remote device.
     */
    private void closeSocket() {
        try {
            if (inStream != null) {
                inStream.close();
            }
            if (outStream != null) {
                outStream.flush();
                outStream.close();
            }
            if (btSocket != null) {
                btSocket.close();
            }
        } catch (IOException e) {
            Log.e(LED_MATRIX_BT_CONN, "Failed to close socket!");
        }
    }

    /**
     * Connects to the remote device. Only invoke when devices are paired.
     *
     * @return True if the connection to the remote device was successfully
     *         established, false otherwise.
     */
    public boolean connect() {
        // Return false right away devices are not paired.
        if (mAddress == null) {
            return false;
        }

        Log.d(LED_MATRIX_BT_CONN, "Connecting to device with name " + mRemoteBTDeviceName + " (" + mAddress + ")");

        // Get device object for the address.
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mAddress);
	
		/*
		 * This is a workaround to get the socket. Needed because the regular method
		 * apparently doesn't work on many devices. The normal way to get the socket is:
		 * 
		 *        btSocket = device.createRfcommSocketToServiceRecord(SERVICE_UUID);
		 *        
		 * http://stackoverflow.com/questions/2853790/why-cant-htc-droid-running-ota-2-1-communicate-with-rfcomm
		 */
        try {
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
            btSocket = (BluetoothSocket) m.invoke(device, 16);
        } catch (Exception e) {
            Log.e(LED_MATRIX_BT_CONN, "Socket creation failed.", e);
            return false;
        }

        try {
            // Blocking connect() call.
            btSocket.connect();
            Log.d(LED_MATRIX_BT_CONN, "Connected to " + mAddress + ".");
        } catch (IOException e) {
            try {
                // Failed to connect. Try to close socket.
                btSocket.close();
            } catch (IOException e2) {
                Log.e(LED_MATRIX_BT_CONN, "Failed to open socket and then failed to close it.", e2);
                return false;
            }
        }

        // Create output and input stream to exchange data with the server.
        try {
            outStream = btSocket.getOutputStream();
            inStream = btSocket.getInputStream();
        } catch (IOException e) {
            Log.e(LED_MATRIX_BT_CONN, "Failed to create output stream.", e);
            return false;
        }

        // Craft handshake packet. See protocol specification for more information.
        byte[] handshake = new byte[5 + mAppName.length()];
        handshake[0] = VERSION;
        handshake[1] = (byte) mXSize;
        handshake[2] = (byte) mYSize;
        handshake[3] = (byte) mColorMode;
        handshake[4] = (byte) mAppName.length();
        System.arraycopy(mAppName.getBytes(), 0, handshake, 5, mAppName.length());

        // Send initial packet to server and wait (blocking) for response.
        this.write(handshake);

        int handshakeResponse = 0;

        try {
            handshakeResponse = inStream.read();
            mMaxFPS = inStream.read();
        } catch (IOException e) {
            Log.e(LED_MATRIX_BT_CONN, "No handshake response from server.", e);
            return false;
        }

        // Check response. TODO: Handle non-zero (error) responses.
        if (handshakeResponse != 0) {
            Log.e(LED_MATRIX_BT_CONN, "Response from server: Handshake error. (Code: " + handshakeResponse + ")");
            return false;
        }

        Log.d(LED_MATRIX_BT_CONN, "Connection established. Maximum supported FPS: " + mMaxFPS + ".");

        // Connection to remote device established. Return true.
        return true;
    }

    /**
     * Closed the connection to the remote device.
     */
    public void closeConnection() {
        this.closeSocket();
    }

    /**
     * Sends the given data to the remote Bluetooth device.
     *
     * @param msgBuffer The data to send to the remote device.
     * @return True if the data was successfully sent, false otherwise.
     */
    public boolean write(byte[] msgBuffer) {
        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            Log.e(LED_MATRIX_BT_CONN, "Exception during write.", e);
            return false;
        }

        return true;
    }

}



