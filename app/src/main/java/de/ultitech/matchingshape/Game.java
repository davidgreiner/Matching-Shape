package de.ultitech.matchingshape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by davidgreiner on 1/27/15.
 */
public class Game {
    private Screen screen;
    private GameMode mode;
    public enum GameState { INTRO, PLAYING, GAMEOVER};
    private GameState state;
    private ShapeGenerator generator;
    private HashMap<Integer, Shape> shapePool;
    private Random random = new Random();

    private int lifes;
    private int counter;

    private boolean tilesMatch;
    private boolean buttonPressed;

    public Game() {
        this(new GameMode());
    }

    public Game(GameMode mode) {
        screen = new Screen();
        this.mode = mode;
        state = GameState.INTRO;
        generator = new ShapeGenerator();
        shapePool = new HashMap<Integer, Shape>();
        counter = 30;
        lifes = 4;
        screen.addToScreen(generator.generateT(), 0);
        screen.addToScreen(generator.generateE(), 1);
        screen.addToScreen(generator.generateC(), 2);
        screen.addToScreen(generator.generateO(), 3);

        for(int i = 0; i < mode.shapePool; i++) {
            Shape s;
            do {
                s = generator.generateRandom(mode.allowRotate, mode.allowShift);
            } while(shapePool.containsValue(s));
            shapePool.put(i, s);
        }
    }

    public void mainLoop() {
        switch(state) {
            case INTRO:
                if(--counter <= 0) {
                    state = GameState.PLAYING;
                    lifes = 4;
                    generateLevel();
                    counter = mode.time;
                }
                break;
            case PLAYING:
                if(--counter <= 0) {
                    if((tilesMatch && !buttonPressed) || (!tilesMatch && buttonPressed)) {
                        buttonPressed = false;
                        --lifes;
                        if(lifes <= 0) {
                            state = GameState.GAMEOVER;
                            Shape X = generator.generateCross();
                            screen.addToScreen(X, 0);
                            screen.addToScreen(X, 1);
                            screen.addToScreen(X, 2);
                            screen.addToScreen(X, 3);
                            return;
                        }
                    }
                    generateLevel();
                    counter = mode.time;
                }
                break;
            case GAMEOVER:
                break;
        }
    }

    public byte[] draw() {
        return screen.drawScreen();
    }

    public void startButtonClicked() {
        switch(state) {
            case INTRO:
                state = GameState.PLAYING;
                break;
            case PLAYING:
                buttonPressed = true;
                counter = 0;
                break;
            case GAMEOVER:
                state = GameState.INTRO;
                screen.addToScreen(generator.generateT(), 0);
                screen.addToScreen(generator.generateE(), 1);
                screen.addToScreen(generator.generateC(), 2);
                screen.addToScreen(generator.generateO(), 3);
                counter = mode.time;
                break;
        }
    }

    private void generateLevel() {
        if(getRandomBoolean(0.25f)) {
            tilesMatch = true;
            // Tiles should match
            ArrayList<Shape> shapes = new ArrayList<Shape>();
            Shape s1 = shapePool.get(random.nextInt(shapePool.size()));
            shapes.add(s1);
            shapes.add(s1);
            Shape s2;
            do {
                s2 = shapePool.get(random.nextInt(shapePool.size()));
            } while(s1.equals(s2));
            shapes.add(s2);
            Shape s3;
            do {
                s3 = shapePool.get(random.nextInt(shapePool.size()));
            } while(s3.equals(s1) || s3.equals(s2));
            shapes.add(s3);
            for(int i = 0; i < 4; i++) {
                screen.addToScreen(shapes.remove(random.nextInt(shapes.size())), i);
            }
        } else {
            tilesMatch = false;
            // Tiles should not match
            HashSet<Shape> shapes = new HashSet<Shape>();
            while(shapes.size() < 4)
                shapes.add(shapePool.get(random.nextInt(shapePool.size())));
            int i = 0;
            for(Shape s : shapes) {
                screen.addToScreen(s, i++);
            }
        }
    }

    private boolean getRandomBoolean(float p){
        return random.nextFloat() < p;
    }

    public GameState getState() { return state; }
}
