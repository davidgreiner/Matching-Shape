package de.ultitech.matchingshape;

/**
 * Created by davidgreiner on 1/27/15.
 */
public class GameMode {
    public int time; // Time user has to decide if tiles match or no
    public int shapePool; // Predefined shapes used to create puzzle tiles
    public boolean allowRotate; // Can the shape be rotated
    public boolean allowMix; // Can the shape be shifted

    public GameMode() {
        this(30, 5, false, false);
    }

    public GameMode(int time, int shapePool, boolean allowRotate, boolean allowMix) {
        this.time = time;
        this.shapePool = shapePool;
        this.allowRotate = allowRotate;
        this.allowMix = allowMix;
    }
}
