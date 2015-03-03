package de.ultitech.matchingshape;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by davidgreiner on 1/20/15.
 */
public class ShapeGenerator {
    private ArrayList<Shape> shapes;
    private Random random = new Random();

    ShapeGenerator() {
        shapes = new ArrayList<Shape>();
        shapes.add(generateB());
        shapes.add(generateC());
        shapes.add(generateCross());
        shapes.add(generateE());
        shapes.add(generateF());
        shapes.add(generateO());
        shapes.add(generateT());
        shapes.add(generateTriangle());
    }

    Shape generateCross()
    {
        return new Shape(new int[][]{
                {255,0,0,0,0,0,0,0,0,0,0,255},
                {0,255,0,0,0,0,0,0,0,0,255,0},
                {0,0,255,0,0,0,0,0,0,255,0,0},
                {0,0,0,255,0,0,0,0,255,0,0,0},
                {0,0,0,0,255,0,0,255,0,0,0,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,255,0,0,255,0,0,0,0},
                {0,0,0,255,0,0,0,0,255,0,0,0},
                {0,0,255,0,0,0,0,0,0,255,0,0},
                {0,255,0,0,0,0,0,0,0,0,255,0},
                {255,0,0,0,0,0,0,0,0,0,0,255}
        });
    }

    Shape generateTriangle()
    {
        return new Shape(new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,255,0,0,255,0,0,0,0},
                {0,0,0,255,0,0,0,0,255,0,0,0},
                {0,0,255,0,0,0,0,0,0,255,0,0},
                {0,255,0,0,0,0,0,0,0,0,255,0},
                {255,255,255,255,255,255,255,255,255,255,255,255},
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0}
        });
    }

    Shape generateT()
    {
        return new Shape(new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,255,255,255,255,255,255,255,255,255,255,0},
                {0,255,255,255,255,255,255,255,255,255,255,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,0,255,255,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0}
        });
    }

    Shape generateE()
    {
        return new Shape(new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0}
        });
    }

    Shape generateF()
    {
        return new Shape(new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0}
        });
    }

    Shape generateB()
    {
        return new Shape(new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,0,0,255,255,0,0,0},
                {0,0,0,255,255,0,0,0,0,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,0,0,255,255,0,0,0},
                {0,0,0,255,255,0,0,255,255,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,255,255,255,255,255,255,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0}
        });
    }

    Shape generateC()
    {
        return new Shape(new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,255,255,255,255,255,255,255,255,255,255,0},
                {0,255,255,255,255,255,255,255,255,255,255,0},
                {0,255,255,0,0,0,0,0,0,0,0,0},
                {0,255,255,0,0,0,0,0,0,0,0,0},
                {0,255,255,0,0,0,0,0,0,0,0,0},
                {0,255,255,0,0,0,0,0,0,0,0,0},
                {0,255,255,0,0,0,0,0,0,0,0,0},
                {0,255,255,0,0,0,0,0,0,0,0,0},
                {0,255,255,255,255,255,255,255,255,255,255,0},
                {0,255,255,255,255,255,255,255,255,255,255,0},
                {0,0,0,0,0,0,0,0,0,0,0,0}
        });
    }

    Shape generateO()
    {
        return new Shape(new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,255,255,255,255,255,255,255,255,255,255,0},
                {0,255,255,255,255,255,255,255,255,255,255,0},
                {0,255,255,0,0,0,0,0,0,255,255,0},
                {0,255,255,0,0,0,0,0,0,255,255,0},
                {0,255,255,0,0,0,0,0,0,255,255,0},
                {0,255,255,0,0,0,0,0,0,255,255,0},
                {0,255,255,0,0,0,0,0,0,255,255,0},
                {0,255,255,0,0,0,0,0,0,255,255,0},
                {0,255,255,255,255,255,255,255,255,255,255,0},
                {0,255,255,255,255,255,255,255,255,255,255,0},
                {0,0,0,0,0,0,0,0,0,0,0,0}
        });
    }

    Shape generateRandom(boolean rotate, boolean shift)
    {
        Shape s = shapes.get(random.nextInt(shapes.size()));
        if(rotate) {
            for(int i = 0; i < random.nextInt(3); i++)
                s.rotate();
        }
        return s;
    }
}
