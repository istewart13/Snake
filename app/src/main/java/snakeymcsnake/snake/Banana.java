package snakeymcsnake.snake;

import java.util.Random;

/**
 * Created by user on 24/08/2016.
 */
public class Banana implements Edible {
    private int mXPos;
    private int mYPos;
    private int mPoints;

    public Banana(int numBlocksWide, int numBlocksHigh) {
        Random randInt = new Random();
        mXPos = randInt.nextInt(numBlocksWide - 1) + 1;
        mYPos = randInt.nextInt(numBlocksHigh - 1) + 1;
        mPoints = 5;
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
