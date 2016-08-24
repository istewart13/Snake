package snakeymcsnake.snake;

import java.util.Random;

/**
 * Created by user on 23/08/2016.
 */
public class Apple implements Edible{
    private int mXPos;
    private int mYPos;
    private int mPoints;

    public Apple(int numBlocksWide, int numBlocksHigh) {
        Random randInt = new Random();
        mXPos = randInt.nextInt(numBlocksWide - 1) + 1;
        mYPos = randInt.nextInt(numBlocksHigh - 1) + 1;
        mPoints = 1;
    }

    public int getXPos() {
        return mXPos;
    }

    public int getYPos() {
        return mYPos;
    }

    public int getPoints() {
        return mPoints;
    }
}
