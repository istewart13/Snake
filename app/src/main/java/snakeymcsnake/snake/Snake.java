package snakeymcsnake.snake;

/**
 * Created by user on 22/08/2016.
 */
public class Snake {
    private int [] mSnakeXPos;
    private int [] mSnakeYPos;
    private int mSnakeLength;
    private boolean mAlive = true;
    private int mNumBlocksWide;
    private int mNumBlocksHigh;
    private int mDirection = 1; // right = 1, down = 2, left = 3, up = 4

    public Snake(int numBlocksWide, int numBlocksHigh) {
        mNumBlocksWide = numBlocksWide;
        mNumBlocksHigh = numBlocksHigh;
        mSnakeXPos = new int[200];
        mSnakeYPos = new int[200];
        mSnakeLength = 3;
        createInitialSnake();
    }

    private void createInitialSnake() {
        int startXPos = mNumBlocksWide / 2;
        int startYPos = mNumBlocksHigh / 2;

        mSnakeXPos[0] = startXPos;
        mSnakeXPos[1] = startXPos - 1;
        mSnakeXPos[2] = startXPos - 2;

        mSnakeYPos[0] = startYPos;
        mSnakeYPos[1] = startYPos;
        mSnakeYPos[2] = startYPos;
    }

    public void grow() {
        mSnakeLength++;
    }

    public void moveBody() {
        for (int i = mSnakeLength; i > 0; i--) {
            mSnakeXPos[i] = mSnakeXPos[i - 1];
            mSnakeYPos[i] = mSnakeYPos[i - 1];
        }
    }

    public void moveHead() {
        switch (mDirection) {
            case 1: // right
                mSnakeXPos[0]++;
                break;
            case 2: // down
                mSnakeYPos[0]++;
                break;
            case 3: // left
                mSnakeXPos[0]--;
                break;
            case 4: // up
                mSnakeYPos[0]--;
                break;
        }
    }

    public int[] getXPos() {
        return mSnakeXPos;
    }

    public int getHeadXPos() {
        return mSnakeXPos[0];
    }

    public int[] getYPos() {
        return mSnakeYPos;
    }

    public int getHeadYPos() {
        return mSnakeYPos[0];
    }

    public int getLength() {
        return mSnakeLength;
    }

    public boolean isAlive() {
        return mAlive;
    }

    public void checkForCollision() {
        boolean leftWallHit = mSnakeXPos[0] < 0;
        boolean rightWallHit = mSnakeXPos[0] > mNumBlocksWide;
        boolean topWallHit = mSnakeYPos[0] < 0;
        boolean bottomWallHit = mSnakeYPos[0] > mNumBlocksHigh;
        boolean snakeHitSelf = collideWithSelf();


        if (leftWallHit || rightWallHit || topWallHit || bottomWallHit || snakeHitSelf) {
            mAlive = false;
        }
    }

    private boolean collideWithSelf() {
        for (int i = 1; i < mSnakeLength; i++) {
            if (mSnakeXPos[0] == mSnakeXPos[i] && mSnakeYPos[0] == mSnakeYPos[i]) {
                return true;
            }
        }
        return false;
    }

    public void increaseDirection() {
        mDirection++;
        if (mDirection == 5) {
            mDirection = 1;
        }
    }

    public void decreaseDirection() {
        mDirection--;
        if (mDirection == 0) {
            mDirection = 4;
        }
    }
}