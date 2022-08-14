package impossibleGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class WinArea extends Rectangle {
    double x;
    double y;
    double width;
    double height;
    boolean isWin;
    boolean isSpawnReset;
    Color fill = Color.LIME;

    public WinArea(double x,double y, double width, double height, boolean isSpawnReset, boolean isWin){
        this. x = x;
        this. y = y;
        this.width = width;
        this.height = height;
        this.isSpawnReset = isSpawnReset;
        this.isWin = isWin;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setFill(fill);
    }
}
