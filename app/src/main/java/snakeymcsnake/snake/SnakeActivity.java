package snakeymcsnake.snake;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

public class SnakeActivity extends AppCompatActivity {


    SnakeView mSnakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get access to screen details
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // create view which adjusts to size of screen
        mSnakeView = new SnakeView(this, size.x, size.y);
        setContentView(mSnakeView);
    }

    // runs when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();
        mSnakeView.resume();
    }

    // runs when the player exits the game
    @Override
    protected void onPause() {
        super.onPause();
        mSnakeView.pause();
    }
}