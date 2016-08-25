package snakeymcsnake.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by user on 22/08/2016.
 */
public class SnakeView extends SurfaceView implements Runnable {

    private Context mContext;
    private Thread mGameThread = null;
    private SurfaceHolder mOurHolder; // locks the surface before drawing graphics
    private volatile boolean mIsPlaying;
    private Paint mPaint;
    private Canvas mCanvas;
    private int mScore;

    // variables related to screen / display
    private long mLastFrameTime;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mScoreDisplayArea;
    private int mBlockSize;
    private int mNumBlocksWide;
    private int mNumBlocksHigh;

    // used to initialise other classes
    private Snake mSnake;
    private Apple mApple;
    private Banana mBanana;
    private Poison mPoison;

    public SnakeView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.mContext = context;
        mOurHolder = getHolder();
        mPaint = new Paint();
        createDisplay(screenWidth, screenHeight);
        setupGameObjects();
    }

    private void createDisplay(int screenWidth, int screenHeight) {
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mScoreDisplayArea = mScreenHeight / 15;
        mBlockSize = mScreenWidth / 50;
        mNumBlocksWide = 50;
        mNumBlocksHigh = (mScreenHeight - (mScoreDisplayArea * 2)) / mBlockSize;
    }

    private void setupGameObjects() {
        mSnake = new Snake(mNumBlocksWide, mNumBlocksHigh);
        mApple = new Apple(mNumBlocksWide, mNumBlocksHigh);
        mBanana = new Banana(mNumBlocksWide, mNumBlocksHigh);
        mPoison = new Poison(mNumBlocksWide, mNumBlocksHigh);
    }

    @Override
    public void run() { // game loop
        while (mIsPlaying) {
            update();
            draw();
            controlFramesPerSecond();
        }
    }

    private void update() {
        checkIfSnakeHasAte();
        mSnake.moveBody();
        mSnake.moveHead();
        mSnake.checkForCollision();
        if (!mSnake.isAlive()) { // reset if game over
            mScore = 0;
            setupGameObjects();
        }
    }

    private void draw() {
        if (mOurHolder.getSurface().isValid()) {
            mCanvas = mOurHolder.lockCanvas();

            // clear the canvas
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            // draw background colour
            mCanvas.drawColor(Color.argb(255, 77, 189, 51));

            // draw food / poison
            drawApple();
            drawBanana();
            drawPoison();

            // change brush colour to white
            mPaint.setColor(Color.argb(255, 255, 255, 255));

            // draw snake
            drawSnake();

            // draw score / length
            mPaint.setTextSize(60);
            mCanvas.drawText("Score: " + mScore + " Length: " + mSnake.getLength(), 10, 70, mPaint);

            // draw everything to screen
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void drawApple() {
        int XPos = mApple.getXPos();
        int YPos = mApple.getYPos();
        mPaint.setColor(Color.argb(255, 255, 0, 0));
        drawBlock(XPos, YPos);
    }

    private void drawBanana() {
        int XPos = mBanana.getXPos();
        int YPos = mBanana.getYPos();
        mPaint.setColor(Color.argb(255, 255, 225, 53));
        drawBlock(XPos, YPos);
    }

    private void drawPoison() {
        int XPos = mPoison.getXPos();
        int YPos = mPoison.getYPos();
        mPaint.setColor(Color.argb(255, 0, 0, 0));
        drawBlock(XPos, YPos);
    }

    private void drawSnake() {
        int [] snakeXPos = mSnake.getXPos();
        int [] snakeYPos = mSnake.getYPos();
        int snakeLength = mSnake.getLength();

        for (int i = 0; i < snakeLength; i++) {
            drawBlock(snakeXPos[i], snakeYPos[i]);
        }
    }

    private void drawBlock(int XPos, int YPos) {
        mCanvas.drawRect((XPos * mBlockSize),
                (YPos * mBlockSize + mScoreDisplayArea),
                (XPos * mBlockSize + mBlockSize),
                (YPos * mBlockSize + mScoreDisplayArea + mBlockSize),
                mPaint);
    }

    private void controlFramesPerSecond() {
        long timeThisFrame = System.currentTimeMillis() - mLastFrameTime;
        long sleepTime = mSnake.getSpeed() - timeThisFrame;

        if (sleepTime > 0) {
            try {
                mGameThread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
        }
        mLastFrameTime = System.currentTimeMillis();
    }

    private void checkIfSnakeHasAte() {
        if (mSnake.checkIfAte(mApple)) {
            mScore += mApple.getPoints();
            mApple = new Apple(mNumBlocksWide, mNumBlocksHigh);
        } else if (mSnake.checkIfAte(mBanana)) {
            mScore += mBanana.getPoints();
            mBanana = new Banana(mNumBlocksWide, mNumBlocksHigh);
        } else if (mSnake.checkIfAte(mPoison)) {
            mScore += mPoison.getPoints();
            mPoison = new Poison(mNumBlocksWide, mNumBlocksHigh);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (motionEvent.getX() > mScreenWidth / 2) {
                    mSnake.increaseDirection(); // click on right side of screen
                } else {
                    mSnake.decreaseDirection(); // click on left side of screen
                }
        }
        return true;
    }

    // if the game stops, then stop this thread
    public void pause() {
        mIsPlaying = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    // if the game starts, then start this thread
    public void resume() {
        mIsPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }
}