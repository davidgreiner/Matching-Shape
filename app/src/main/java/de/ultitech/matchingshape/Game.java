package de.ultitech.matchingshape;

/**
 * Created by davidgreiner on 1/27/15.
 */
public class Game {
    private Screen screen;
    private GameMode mode;
    private enum GameState { INTRO, PLAYING, GAMEOVER};
    private GameState state;
    private ShapeGenerator generator;

    public Game() {
        this(new GameMode());
    }

    public Game(GameMode mode) {
        screen = new Screen();
        this.mode = mode;
        state = GameState.INTRO;
        generator = new ShapeGenerator();
    }

    public void mainLoop() {
        switch(state) {
            case INTRO:
                Shape T = generator.generateT();
                Shape E = generator.generateE();
                Shape C = generator.generateC();
                Shape O = generator.generateO();
                screen.addToScreen(T, 0);
                screen.addToScreen(E, 1);
                screen.addToScreen(C, 2);
                screen.addToScreen(O, 3);
                break;
            case PLAYING:
                Shape T1 = generator.generateT();
                Shape E1 = generator.generateE();
                Shape C1 = generator.generateC();
                Shape O1 = generator.generateO();
                screen.addToScreen(T1, 0);
                screen.addToScreen(E1, 2);
                screen.addToScreen(C1, 1);
                screen.addToScreen(O1, 3);
                break;
            case GAMEOVER:
                Shape X = generator.generateCross();
                screen.addToScreen(X, 0);
                screen.addToScreen(X, 1);
                screen.addToScreen(X, 2);
                screen.addToScreen(X, 3);
                break;
        }
    }

    public byte[] draw() {
        return screen.drawScreen();
    }

    public void buttonClicked() {
        switch(state) {
            case INTRO:
                state = GameState.PLAYING;
                break;
            case PLAYING:
                state = GameState.GAMEOVER;
                break;
            case GAMEOVER:
                state = GameState.INTRO;
                break;
        }
    }
}
