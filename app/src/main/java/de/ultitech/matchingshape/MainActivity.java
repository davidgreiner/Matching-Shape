package de.ultitech.matchingshape;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import at.markushi.ui.CircleButton;
public class MainActivity extends Activity {

	private LEDMatrixBTConn BT;
	protected static final String REMOTE_BT_DEVICE_NAME = "ledpi-teco";

	// Remote display x and y size.
	protected static final int X_SIZE = 24;
	protected static final int Y_SIZE = 24;

	// Remote display color mode. 0 = red, 1 = green, 2 = blue, 3 = RGB.
	protected static final int COLOR_MODE = 0;

	// The name this app uses to identify with the server.
	protected static final String APP_NAME = "MatchingShape";

    private BTThread btThread;

	// The start button.
	private CircleButton mStartButton;

    private enum State { DISCONNECTED, CONNECTED };
    private State appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartButton = (CircleButton) findViewById(R.id.startButton);
        appState = State.DISCONNECTED;
    }


	public void start(View v) {
        switch(appState) {
            case DISCONNECTED:
                // Set up BT connection.
                BT = new LEDMatrixBTConn(this, REMOTE_BT_DEVICE_NAME, X_SIZE, Y_SIZE, COLOR_MODE, APP_NAME);

                if (!BT.prepare())
                    return;

                if (!BT.checkIfDeviceIsPaired())
                    return;

                mStartButton.setImageResource(R.drawable.ic_action_cross);
                btThread = new BTThread(BT);
                btThread.start();
                appState = State.CONNECTED;
                break;
            case CONNECTED:
                btThread.buttonClick();
                break;
        }
	}

	@Override
	public void onPause() {
		super.onPause();

        appState = State.DISCONNECTED;
        mStartButton.setImageResource(R.drawable.ic_action_start);
        // Avoid crash if user exits the app before pressing start.
        if (BT != null) {
            BT.onPause();
        }
	}
}
