package de.ultitech.matchingshape;

import java.util.Arrays;

/**
 * Created by davidgreiner on 1/20/15.
 */
public class Shape {
    private int length = 12;
    private int[][] shape = new int[length][length];

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

    Shape rotate()
    {
        int[][] newArray = new int[length][length];
        for(int i = 0; i < length;i++) {
            for(int j = 0; j < length; j++) {
                newArray[i][j] = shape[length - j - 1][i];
            }
        }
        return new Shape(newArray);
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

    Shape add(Shape s)
    {
        int[][] newArray = new int[length][length];
        for(int i = 0; i < length; i++)
        {
            for (int j = 0; j < length; j++)
            {
                if (s.getPixel(i, j) != 0)
                    newArray[i][j] = Math.min(shape[i][j] + s.getPixel(i, j), 255);
            }
        }
        return new Shape(newArray);
    }

    Shape substract(Shape s)
    {
        int[][] newArray = new int[length][length];
        for(int i = 0; i < length; i++)
        {
            for (int j = 0; j < length; j++)
            {
                if (s.getPixel(i, j) != 0)
                    newArray[i][j] = Math.max(shape[i][j] - s.getPixel(i, j), 0);
            }
        }
        return new Shape(newArray);
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

    int getPixel(int x, int y)
    {
        return shape[x][y];
    }

    int getLength() {return length; }

    int[][] getShape() { return shape; }

    @Override public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Shape) {
            Shape that = (Shape) other;
            result = (that.canEqual(this) && this.getLength() == that.getLength() && Arrays.deepEquals(this.getShape(), that.getShape()));
        }
        return result;
    }

    @Override public int hashCode() {
        int sum = getLength();
        int index = 1;
        for(int i = 0; i < getLength(); i++) {
            for (int j = 0; j < getLength(); j++) {
                sum += index * getPixel(i, j);
                index++;
            }
        }
        return sum;
    }

    public boolean canEqual(Object other) {
        return (other instanceof Shape);
    }
}
