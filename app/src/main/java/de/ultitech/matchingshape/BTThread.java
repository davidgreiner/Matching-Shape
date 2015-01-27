package de.ultitech.matchingshape;

/**
 * Created by davidgreiner on 1/27/15.
 */
public class BTThread extends Thread {
    private LEDMatrixBTConn BT;
    private int sendDelay;
    private boolean loop;
    private boolean button;


    public BTThread(LEDMatrixBTConn BT) {
        this.BT = BT;
        button = false;
        loop = true;
    }

    public void buttonClick() {
        button = true;
    }

    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        // Try to connect.
        if (!BT.connect()) {
            loop = false;
        }

        Screen screen = new Screen();
        ShapeGenerator generator = new ShapeGenerator();
        Shape T = generator.generateT();
        Shape E = generator.generateE();
        Shape C = generator.generateC();
        Shape O = generator.generateO();
        screen.addToScreen(T, 0);
        screen.addToScreen(E, 1);
        screen.addToScreen(C, 2);
        screen.addToScreen(O, 3);

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
            if(button) {
                screen.addToScreen(T, 0);
                screen.addToScreen(E, 2);
                screen.addToScreen(C, 1);
                screen.addToScreen(O, 3);
                button = false;
            }
            byte[] msgBuffer = screen.drawScreen();
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
