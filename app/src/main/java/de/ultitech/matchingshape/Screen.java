package de.ultitech.matchingshape;

/**
 * Created by davidgreiner on 1/22/15.
 */
public class Screen {
    Shape[] shapes = new Shape[4];

    void addToScreen(Shape s,  int i)
    {
        if(0 <= i && i < 4)
            shapes[i] = s;
    }

    byte[] drawScreen()
    {
        byte[] msgBuffer = new byte[24 * 24];
        //Block
        for(int i = 0; i < 4; i++)
        {
            //Row
            for(int k = 0; k < 12; k++)
            {
                //Column
                for (int j = 0; j < 12; j++)
                {
                    int bottom = 0;
                    if(i >= 2)
                        bottom = 1;
                    msgBuffer[j + (i % 2) * 12 + k * 24 + bottom * 12 * 24] = shapes[i].prepareScreen()[j*12+k];
                }
            }
        }
        return msgBuffer;
    }
}
