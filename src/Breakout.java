import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.program.GraphicsProgram;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

import svu.csc213.Dialog;

public class Breakout extends GraphicsProgram {

    // 1) make a lives variable and make it change when you lose the ball
    // 2) make a brickLives variable in Brick
    // 3) make a method endGame when your life count goes to 0
    // 4/5) make a GLabel for lives left and bricks broken

    // 6) in Brick make a random chance that the brick will have a power-up
    //    write method for different power-ups
    // 7) make a new class to make different levels based on how far the player is

    private Ball ball;
    private Paddle paddle;

    private int numBricksInRow;
    private Color[] rowColors = {Color.pink, Color.pink, Color.BLUE, Color.BLUE, Color.CYAN, Color.CYAN, Color.MAGENTA, Color.MAGENTA, Color.LIGHT_GRAY, Color.LIGHT_GRAY};
    private Color[] lifeColors = {rowColors[1], rowColors[3], rowColors[5], rowColors[7], rowColors[9]};
    private int lives, bricksBroken;
    private GLabel livesLabel, bricksBrokenLabel;

    private Random rand;
    private int powerupChance;

    @Override
    public void init() {
        ball = new Ball(getWidth()/2, 350, 10, this.getGCanvas());
        add(ball);

        paddle = new Paddle(230, 430, 50, 10);
        add(paddle);

        numBricksInRow = (int) (getWidth() / (Brick.WIDTH + 5.0));

        rand = new Random();

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < numBricksInRow; col++) {
                double brickX = (10 + col * (Brick.WIDTH + 5));
                double brickY = 4*Brick.HEIGHT + row * (Brick.HEIGHT+5);
                powerupChance = rand.nextInt(20);
                Brick brick = new Brick(brickX, brickY, rowColors[row], row);

                if(powerupChance == 0) {
                    brick.setFillColor(Color.BLACK);
                }

                add(brick);
            }
        }

        lives = 3;
        livesLabel = new GLabel(lives + " lives left");
        add(livesLabel, 5, 15);
        bricksBroken = 0;
        bricksBrokenLabel = new GLabel(bricksBroken + " bricks broken");

    }



    @Override
    public void run() {
        addMouseListeners();
        waitForClick();
        gameLoop();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        //make sure that the paddle doesn't go off-screen
        if((me.getX() < getWidth() - paddle.getWidth()/2)&&(me.getX() > paddle.getWidth()/2)) {
            paddle.setLocation(me.getX() - paddle.getWidth()/2, paddle.getY());
        }
    }

    private void gameLoop() {
        while(true) {
            //move the ball
            ball.handleMove();

            //handle collisions
            handleCollisions();

            //check if lost
            if(ball.lost) {
                handleLoss();
            }
            pause(5);
        }
    }

    private void handleCollisions() {
        //obj can store what we hit
        GObject obj = null;


        //check to see if the ball is about to hit something
        if(obj == null) {
            //check the top right corner
            obj = this.getElementAt(ball.getX() + ball.getWidth(), ball.getY());
        }
        if(obj == null) {
            //check the top left corner
            obj = this.getElementAt(ball.getX(), ball.getY());
        }
        if(obj == null) {
            //check bottom right corner
            obj = this.getElementAt(ball.getX() + ball.getWidth(), ball.getY() + ball.getHeight());
        }
        if(obj == null) {
            //check bottom left corner
            obj = this.getElementAt(ball.getX(), ball.getY() + ball.getHeight());
        }

        //see if we hit something
        if(obj != null) {
            //let's see what we hit
            if(obj instanceof Paddle) {
                if(ball.getX() < (paddle.getX() + paddle.getWidth()*0.15)){
                    //did I hit the left side of the paddle?
                    ball.bounceLeft();
                } else if(ball.getX() > (paddle.getX()+ paddle.getWidth()*0.85)) {
                    //did I hit the right side of the paddle?
                    ball.bounceRight();
                } else {
                    ball.bounce();
                }
            }

            if(obj instanceof Brick) {
                ball.bounce();


                if(((Brick) obj).getFillColor() == lifeColors[0]) {
                    ((Brick) obj).setFillColor(lifeColors[1]);
                } else if(((Brick) obj).getFillColor() == lifeColors[1]) {
                    ((Brick) obj).setFillColor(lifeColors[2]);
                } else if(((Brick) obj).getFillColor() == lifeColors[2]) {
                    ((Brick) obj).setFillColor(lifeColors[3]);
                } else if(((Brick) obj).getFillColor() == lifeColors[3]) {
                    ((Brick) obj).setFillColor(lifeColors[4]);
                } else if(((Brick) obj).getFillColor() == Color.BLACK   ) {
                    doPowerUps();
                    this.remove(obj);
                    bricksBroken++;
                    bricksBrokenLabel.setVisible(false);
                    bricksBrokenLabel = new GLabel(bricksBroken + " bricks broken");
                    add(bricksBrokenLabel, livesLabel.getX() + livesLabel.getWidth() + 5, livesLabel.getY());
                } else {
                    this.remove(obj);
                    bricksBroken++;
                    bricksBrokenLabel.setVisible(false);
                    bricksBrokenLabel = new GLabel(bricksBroken + " bricks broken");
                    add(bricksBrokenLabel, livesLabel.getX() + livesLabel.getWidth() + 5, livesLabel.getY());
                }
            }
        }
    }

    private void handleLoss() {
        lives--;
        livesLabel.setVisible(false);
        livesLabel = new GLabel(lives + " lives left");
        add(livesLabel, 5, 15);
        if(lives == 0) {
            Dialog.showMessage("You Lost!");
            exit();
        } else {
            reset();
            ball.lost = false;
        }
    }

    private void reset() {
        ball.setLocation(getWidth()/2, 350);
        paddle.setLocation(getWidth()/2, 430);
        waitForClick();
    }

    private void doPowerUps() {
            switch(rand.nextInt(2)) {
                case 0:
                    paddle.setBounds(paddle.getX(), paddle.getY(), paddle.getWidth() + 20, paddle.getHeight());
                    break;
                case 1:
                    lives++;
                    livesLabel.setLabel(lives + " lives left");
                    break;
            }
    }


    public static void main(String[] args) {
        new Breakout().start();
    }
}
