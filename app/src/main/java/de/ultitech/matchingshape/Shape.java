package de.ultitech.matchingshape;

/**
 * Created by davidgreiner on 1/20/15.
 */
public class Shape {
    int length = 12;
    int[][] shape = new int[length][length];

    Shape()
    {
        for(int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                shape[i][j] = 255;
            }
        }
    }

    Shape(int[][] shape)
    {
        this.shape = shape;
    }

    void rotate()
    {
        int[][] newArray = new int[length][length];
        for(int i = 0; i < length;i++) {
            for(int j = 0; j < length; j++) {
                newArray[i][j] = shape[length - j - 1][i];
            }
        }
        shape = newArray;
    }

    void changeBrightness(int brightness)
    {
        for(int i = 0; i < length; i++) {
            for(int j = 0; j < length; j++) {
                if(shape[i][j] != 0)
                {
                    shape[i][j] = brightness;
                }
            }
        }
    }

    byte[] prepareScreen()
    {
        byte[] bytes = new byte[length * length];
        for(int i = 0; i < shape.length; i++)
        {
            for(int j = 0; j < shape[i].length; j++)
            {
               bytes[j*length+i] = (byte) shape[i][j];
            }
        }
        return bytes;
    }
}
