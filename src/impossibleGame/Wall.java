package impossibleGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall extends Rectangle {
    double x;
    double y;
    double width;
    double height;
    boolean invisible;

    public Wall(double x, double y, double width, double height, boolean invisible){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.invisible = invisible;
        setX(x);
        setY(y);
        setFill(Color.BLACK);
        setWidth(width);
        setHeight(height);
    }
}
