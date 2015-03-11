package de.ultitech.matchingshape;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;


import at.markushi.ui.CircleButton;
public class MainActivity extends Activity {

	private LEDMatrixBTConn BT;
	protected static String REMOTE_BT_DEVICE_NAME;

	// Remote display x and y size.
	protected static final int X_SIZE = 24;
	protected static final int Y_SIZE = 24;

	// Remote display color mode. 0 = red, 1 = green, 2 = blue, 3 = RGB.
	protected static final int COLOR_MODE = 0;

	// The name this app uses to identify with the server.
	protected static final String APP_NAME = "MatchingShape";

    private BTThread btThread;

	// The start button.
	private static CircleButton mStartButton;

    // Life display
    private static ImageView[] mLifes = new ImageView[4];

    private static RadioGroup difficultyRadioButtonGroup;

    private enum State { DISCONNECTED, CONNECTED }
    private State appState;

    private static Vibrator v;
    private static SharedPreferences sharedPref;

    public static Handler UIHandler;


    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }
    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        REMOTE_BT_DEVICE_NAME = sharedPref.getString(SettingsActivity.BT_SELECT_DEVICE_KEY, "");

        mStartButton = (CircleButton) findViewById(R.id.startButton);
        mLifes[0] = (ImageView) findViewById(R.id.imageView0);
        mLifes[1] = (ImageView) findViewById(R.id.imageView1);
        mLifes[2] = (ImageView) findViewById(R.id.imageView2);
        mLifes[3] = (ImageView) findViewById(R.id.imageView3);
        difficultyRadioButtonGroup = (RadioGroup) findViewById(R.id.difficulty);
        appState = State.DISCONNECTED;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void lifeViewSet(final int i)
    {
        runOnUI(new Runnable() {
            @Override
            public void run() {
                if(i >= 0 && i < 4) {
                    mLifes[i].setImageResource(R.drawable.ic_action_cross_red);
                    if(sharedPref.getBoolean(SettingsActivity.GAMEPLAY_VIBRATE, true))
                        v.vibrate(50);
                }
            }
        });
    }

    public static void lifeViewReset() {
        runOnUI(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 4; i++)
                {
                     mLifes[i].setImageResource(R.drawable.ic_action_cross);
                }
            }
        });
    }

    public static void setStart() {
        runOnUI(new Runnable() {
            @Override
            public void run() {
                mStartButton.setImageResource(R.drawable.ic_action_start);
            }
        });
    }

    public static void setCross() {
        runOnUI(new Runnable() {
            @Override
            public void run() {
                mStartButton.setImageResource(R.drawable.ic_action_cross);
            }
        });
    }

    public static int getDifficulty() {
        int radioButtonID = difficultyRadioButtonGroup.getCheckedRadioButtonId();
        View radioButton = difficultyRadioButtonGroup.findViewById(radioButtonID);
        return difficultyRadioButtonGroup.indexOfChild(radioButton);
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
                btThread.startClick();
                break;
        }
	}

    public void stop(View v) {
        btThread.stopClick();
    }

	@Override
	public void onPause() {
		super.onPause();

        appState = State.DISCONNECTED;
        mStartButton.setImageResource(R.drawable.ic_action_start);
        lifeViewReset();
        // Avoid crash if user exits the app before pressing start.
        if (BT != null) {
            BT.onPause();
        }
	}

    @Override
    protected void onResume() {
        super.onResume();
        if(REMOTE_BT_DEVICE_NAME.equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("Set a ConnectionMachine")
                    .setMessage("Select a paired device to use this app with.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(((Dialog) dialog).getContext(), SettingsActivity.class));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
