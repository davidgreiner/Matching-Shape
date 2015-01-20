package de.ultitech.matchingshape;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

	// The start button.
	private Button mStartButton;
	private int sendDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartButton = (Button) findViewById(R.id.startButton);
    }


	public void start(View v) {
		mStartButton.setEnabled(false);

		// Set up BT connection.
        // Set up BT connection.
        BT = new LEDMatrixBTConn(this, REMOTE_BT_DEVICE_NAME, X_SIZE, Y_SIZE, COLOR_MODE, APP_NAME);

        if (!BT.prepare() || !BT.checkIfDeviceIsPaired()) {
            mStartButton.setEnabled(true);
            return;
        }

        // Start BT sending thread.
        Thread sender = new Thread() {

            boolean loop = true;
            public void run() {

                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

                // Try to connect.
				if (!BT.connect()) {
					loop = false;
				}

                ShapeGenerator generator = new ShapeGenerator();
                Shape cross = generator.generateCross();
                Shape triangle = generator.generateTriangle();
                triangle.rotate();
                cross.changeBrightness(125);
                byte[] crossScreen = triangle.prepareScreen();

				// Connected. Calculate and set send delay from maximum FPS.
                // Negative maxFPS should not happen.
                int maxFPS = BT.getMaxFPS();
                if (maxFPS > 0) {
                    sendDelay = (int) (1000.0 / maxFPS);
                } else {
                    loop = false;
                }

                // Prepare variables for making the pattern.
				int counter = 0;
				int a = 255;
				int b = 0;

				// Main sending loop.
				while (loop) {
					//counter++;

                    byte[] msgBuffer = new byte[24 * 24];
                    //Block
                    for(int i = 0; i < 4; i++)
                    {
                        //Row
                        for(int k = 0; k < 12; k++)
                        {
                            //Column
                             for (int j = 0; j < 12; j++)
                             {
                                 //msgBuffer[j + i * 12 * 12] = crossScreen[j];
                                 int bottom = 0;
                                 if(i >= 2)
                                     bottom = 1;
                                 msgBuffer[j + (i % 2) * 12 + k * 24 + bottom * 12 * 24] = crossScreen[j*12+k];
                             }
                        }
                    }
                    /*
					// Change pattern every 10 frames.
					if (counter >= 10) {
						if (a == 255) {
							a = 0;
							b = 255;
						} else {
							a = 255;
							b = 0;
						}
						counter = 0;
					}

					// Fill message buffer.
					byte[] msgBuffer = new byte[24 * 24];
					for (int i = 0; i < (24 * 24); i++) {
						if (i % 2 == 1) {
							msgBuffer[i] = (byte) a;
						} else {
							msgBuffer[i] = (byte) b;
				    	}
					}
                    */
					// If write fails, the connection was probably closed by the server.
					if (!BT.write(msgBuffer)) {
						loop = false;
					}

					try {
						// Delay for a moment.
                        // Note: Delaying the same amount of time every frame will not give you constant FPS.
						Thread.sleep(sendDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

                // Connection terminated or lost.
                BT.closeConnection();
			}
		};

		// Start sending thread.
		sender.start();
	}

	@Override
	public void onPause() {
		super.onPause();

        mStartButton.setEnabled(true);

        // Avoid crash if user exits the app before pressing start.
        if (BT != null) {
            BT.onPause();
        }
	}
}
