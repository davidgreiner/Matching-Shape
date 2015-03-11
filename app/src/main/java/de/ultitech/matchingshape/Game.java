package de.ultitech.matchingshape;

import android.util.Log;

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
    public enum GameState { INTRO, PLAYING, GAMEOVER}
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
        setDifficulty();
        state = GameState.INTRO;
        generator = new ShapeGenerator();
        shapePool = new HashMap<>();
        counter = 30;
        lifes = 4;
        screen.addToScreen(generator.generateT(), 0);
        screen.addToScreen(generator.generateE(), 1);
        screen.addToScreen(generator.generateC(), 2);
        screen.addToScreen(generator.generateO(), 3);

        fillShapePool();
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
                        tilesMatch = false;
                        --lifes;
                        MainActivity.lifeViewSet(lifes);
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
                MainActivity.lifeViewReset();
                setDifficulty();
                fillShapePool();
                counter = mode.time;
                break;
        }
    }

    private void generateLevel() {
        if(getRandomBoolean(0.25f)) {
            tilesMatch = true;
            // Tiles should match
            ArrayList<Shape> shapes = new ArrayList<>();
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
            HashSet<Shape> shapes = new HashSet<>();
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

    private void setDifficulty() {
        switch(MainActivity.getDifficulty()) {
            case 1:
                Log.d("Debug", "Medium");
                this.mode.time = 20;
                this.mode.shapePool = 20;
                this.mode.allowRotate = true;
                this.mode.allowMix = false;
                break;
            case 2:
                Log.d("Debug", "Hard");
                this.mode.time = 10;
                this.mode.shapePool = 30;
                this.mode.allowRotate = true;
                this.mode.allowMix = true;
                break;
            default:
                Log.d("Debug", "Easy");
                this.mode.time = 30;
                this.mode.shapePool = 10;
                this.mode.allowRotate = false;
                this.mode.allowMix = false;
                break;
        }
    }

    private void fillShapePool() {
        shapePool.clear();
        for(int i = 0; i < mode.shapePool; i++) {
            Shape s;
            do {
                s = generator.generateRandom(mode.allowRotate, mode.allowMix);
            } while(shapePool.containsValue(s));
            shapePool.put(i, s);
        }
    }

    public GameState getState() { return state; }
}
