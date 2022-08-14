package impossibleGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;


public class Circle extends javafx.scene.shape.Circle {
    double x;
    double y;
    double vx;
    double vy;
    double radius = 7;
    Color fill  = Color.BLUEVIOLET;

    public Circle(double x, double y, double vx, double vy){
        this.x = x;
        this.y = y;
        setCenterX(x);
        setCenterY(y);
        setRadius(radius);
        setFill(fill);
        setStroke(Color.BLACK);
        setStrokeWidth(2);
        setStrokeType(StrokeType.INSIDE);
        this.vx = vx;
        this.vy = vy;
    }
    public void drawCircle(ArrayList<Wall> walls){
        this.x += vx;
        this.y += vy;
        setCenterX(x);
        setCenterY(y);
        checkCollisionWall(walls);
    }

    public void checkCollisionWall(ArrayList<Wall> walls) {
        double top = this.y - radius;
        double bot = this.y + radius;
        double left = this.x - radius;
        double right = this.x + radius;
        for (Wall wall : walls) {
            double wTop = wall.y;
            double wBot = wall.y + wall.height;
            double wLeft = wall.x;
            double wRight = wall.x + wall.width;

            double xOverlap = Math.min(right, wRight) - Math.max(left, wLeft);
            double yOverlap = Math.min(bot, wBot) - Math.max(top, wTop);
            if (xOverlap > 0 && yOverlap > 0) {
                if (xOverlap < yOverlap) vx *= -1;
                if (xOverlap > yOverlap) vy *= -1;
            }
        }
    }

}
