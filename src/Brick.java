import acm.graphics.GRect;
import java.awt.Color;
import java.util.Random;

public class Brick extends GRect {

    public static final int WIDTH = 44;
    public static final int HEIGHT = 20;

    private int row;


    public Brick(double x, double y, Color color, int row) {
        super(x, y, WIDTH, HEIGHT);
        this.setFillColor(color);
        this.setFilled(true);
    }
}
