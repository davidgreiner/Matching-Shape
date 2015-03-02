package de.ultitech.matchingshape;

/**
 * Created by davidgreiner on 1/27/15.
 */
public class BTThread extends Thread {
    private LEDMatrixBTConn BT;
    private int sendDelay;
    private boolean loop;
    private boolean startButton;
    private boolean stopButton;


    public BTThread(LEDMatrixBTConn BT) {
        this.BT = BT;
        startButton = false;
        stopButton = false;
        loop = true;
    }

    public void startClick() {
        startButton = true;
    }

    public void stopClick() { stopButton = true; }

    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        // Try to connect.
        if (!BT.connect()) {
            loop = false;
        }

        Game game = new Game(new GameMode(30, 5, true, false));

        // Connected. Calculate and set send delay from maximum FPS.
        // Negative maxFPS should not happen.
        int maxFPS = BT.getMaxFPS();
        if (maxFPS > 0) {
            sendDelay = (int) (1000.0 / maxFPS);
        } else {
            loop = false;
        }

        // Main sending loop.
        while (loop) {
            if(startButton) {
                game.startButtonClicked();
                startButton = false;
            }
            game.mainLoop();
            byte[] msgBuffer = game.draw();
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
}
