package snakeymcsnake.snake;

import java.util.Random;

/**
 * Created by user on 23/08/2016.
 */
public class Apple {
    private int mAppleXPos;
    private int mAppleYPos;

    public Apple(int numBlocksWide, int numBlocksHigh) {
        Random randInt = new Random();
        mAppleXPos = randInt.nextInt(numBlocksWide - 1) + 1;
        mAppleYPos = randInt.nextInt(numBlocksHigh - 1) + 1;
    }

    public int getXPos() {
        return mAppleXPos;
    }

    public int getYPos() {
        return mAppleYPos;
    }
}
