package snakeymcsnake.snake;

import android.content.Context;
import android.graphics.Bitmap;
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
    private boolean mPaused = true;
    private Paint mPaint;
    private Canvas mCanvas;

    // used to initialise other classes
    private Snake mSnake;
    private Apple mApple;
    private Bitmap appleImage;

    // used to control frame rate
    private long mFps; // track frames per second
    private long mTimeThisFrame; // used to calculate fps
    private long mLastFrameTime;

    private int mScreenHeight;
    private int mScreenWidth;
    private int mScoreDisplayArea;

    private int mBlockSize;
    private int mNumBlocksWide;
    private int mNumBlocksHigh;

    // TODO sound variables go here:
//
    private int mScore;
//
//    private int mNumCellsHigh;
//    private int mNumCellsWide;
//    private int mCellSize;

    public SnakeView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.mContext = context;
        mOurHolder = getHolder();
        mPaint = new Paint();

        createDisplay(screenWidth, screenHeight);


//        TODO sound code goes here
        setupGameObjects();
//
//        mSnakeXPos = new int[100];
//        mSnakeYPos = new int[100];
//
//        getSnake(mNumCellsHigh, mNumCellsWide);
//        getApple(mNumCellsHigh, mNumCellsWide);
    }

    @Override
    public void run() {
        while (mIsPlaying) {

//                if (!mPaused) {
            //todo add pause back in
            // update the frame if the games's running

//                }
            update();
            draw();
            controlFramesPerSecond();
        }
    }

    private void update() {
        checkIfAte();
        mSnake.moveBody();
        mSnake.moveHead();
        mSnake.checkForCollision();
        if (!mSnake.isAlive()) {
            mScore = 0;
            setupGameObjects();
            // TODO GAME OVER
        }
    }


    private void draw() {
        if (mOurHolder.getSurface().isValid()) {
            mCanvas = mOurHolder.lockCanvas();

            // clear the canvas
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            // draw background colour
            mCanvas.drawColor(Color.argb(255, 77, 189, 51));

            drawWalls();

            drawApple();

            // change brush colour to white
            mPaint.setColor(Color.argb(255, 255, 255, 255));

            drawSnake();

            mPaint.setTextSize(60);
            mCanvas.drawText("Score: " + mScore, 10, 70, mPaint);

            // draw everything to screen
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP:
                if (motionEvent.getX() > mScreenWidth / 2) {
                    mSnake.increaseDirection();
                } else {
                    mSnake.decreaseDirection();
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

    private void controlFramesPerSecond() {
        long timeThisFrame = System.currentTimeMillis() - mLastFrameTime;
        long sleepTime = 100 - timeThisFrame;

        if (timeThisFrame > 0) {
            mFps = (int) (1000 / timeThisFrame);
        }
        if (sleepTime > 0) {
            try {
                mGameThread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
        }
        mLastFrameTime = System.currentTimeMillis();
    }

    private void createDisplay(int screenWidth, int screenHeight) {
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mScoreDisplayArea = mScreenHeight / 15;
        mBlockSize = mScreenWidth / 50;
        mNumBlocksWide = 50;
        mNumBlocksHigh = (mScreenHeight - (mScoreDisplayArea * 2)) / mBlockSize;

    }

    private void drawWalls() {
//        mPaint.setStrokeWidth(5);
//        mPaint.setColor(Color.argb(255, 139, 69, 19));

    }

    private void drawSnake() {
        int [] snakeXPos = mSnake.getXPos();
        int [] snakeYPos = mSnake.getYPos();
        int snakeLength = mSnake.getLength();

        for (int i = 0; i < snakeLength; i++) {
            mCanvas.drawRect((snakeXPos[i] * mBlockSize),
                    (snakeYPos[i] * mBlockSize + mScoreDisplayArea),
                    (snakeXPos[i] * mBlockSize + mBlockSize),
                    (snakeYPos[i] * mBlockSize + mScoreDisplayArea + mBlockSize),
                    mPaint);
        }
    }

    private void drawApple() {
        int appleXPos = mApple.getXPos();
        int appleYPos = mApple.getYPos();

        mPaint.setColor(Color.argb(255, 255, 0, 0));
        mCanvas.drawRect((appleXPos * mBlockSize),
                (appleYPos * mBlockSize + mScoreDisplayArea),
                (appleXPos * mBlockSize + mBlockSize),
                (appleYPos * mBlockSize + mScoreDisplayArea + mBlockSize),
                mPaint);;
    }

    private void setupGameObjects() {
        // TODO Initialise the game objects here
        mSnake = new Snake(mNumBlocksWide, mNumBlocksHigh);
        mApple = new Apple(mNumBlocksWide, mNumBlocksHigh);
    }

    private void checkIfAte() {
        if (mSnake.getHeadXPos() == mApple.getXPos() && mSnake.getHeadYPos() == mApple.getYPos()) {
            mSnake.grow();
            mApple = new Apple(mNumBlocksWide, mNumBlocksHigh);
            mScore++;
        }
    }


}
