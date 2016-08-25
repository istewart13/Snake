package snakeymcsnake.snake;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by user on 24/08/2016.
 */
public class SnakeTest {
    Snake snake;
    Apple apple;
    Banana banana;
    Poison poison;

    @Before
    public void before(){
        int mNumBlocksWide = 50;
        int mNumBlocksHigh = 40;
        snake = new Snake(mNumBlocksWide, mNumBlocksHigh);
        apple = new Apple(mNumBlocksWide, mNumBlocksHigh);
        banana = new Banana(mNumBlocksWide, mNumBlocksHigh);
        poison = new Poison(mNumBlocksWide, mNumBlocksHigh);
    }

    @Test
    public void snakeIsInitiallyAlive()
    {
        assertEquals(snake.isAlive(), true);
    }

    @Test
    public void getInitialLengthOfSnake()
    {
        assertEquals(snake.getLength(), 3);
    }

    @Test
    public void snakeCreatedInCorrectXPos()
    {
        assertEquals(snake.getXPos()[0], 25);
        assertEquals(snake.getXPos()[1], 24);
        assertEquals(snake.getXPos()[2], 23);
    }

    @Test
    public void snakeCreatedInCorrectYPos()
    {
        assertEquals(snake.getYPos()[0], 20);
        assertEquals(snake.getYPos()[1], 20);
        assertEquals(snake.getYPos()[2], 20);
    }

    @Test
    public void snakeHeadMovesInXDirection()
    {
        snake.moveHead();
        assertEquals(snake.getXPos()[0], 26);

    }

    @Test
    public void snakeBodyMovesInXDirection()
    {
        snake.moveBody();
        assertEquals(snake.getXPos()[1], 25);
        assertEquals(snake.getXPos()[2], 24);
    }

    @Test
    public void snakeCanGrow()
    {
        snake.grow();
        assertEquals(snake.getLength(), 4);
    }

    @Test
    public void snakeCanHitWall()
    {
        for (int i = 0; i < 50; i++) {
            snake.moveHead();
        }
        snake.checkForCollision();
        assertEquals(snake.isAlive(), false);
    }

    @Test
    public void snakeCanTurnRight()
    {
        snake.increaseDirection();
        snake.moveHead();
        assertEquals(snake.getYPos()[0], 21);
    }

    @Test
    public void snakeCanTurnLeft()
    {
        snake.decreaseDirection();
        snake.moveHead();
        assertEquals(snake.getYPos()[0], 19);
    }

    @Test
    public void snakeHasInitialSpeed()
    {
        assertEquals(snake.getSpeed(), 100);
    }

    @Test
    public void appleIsWorth1Point()
    {
        assertEquals(apple.getPoints(), 1);
    }

    @Test
    public void bananaIsWorth5Points()
    {
        assertEquals(banana.getPoints(), 5);
    }

    @Test
    public void poisonIsWorthMinus10Points()
    {
        assertEquals(poison.getPoints(), -10);
    }
}
