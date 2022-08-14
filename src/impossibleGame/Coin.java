package impossibleGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Coin extends Circle {
    double x;
    double y;
    static double radius = 5;
    Color fill = Color.GOLD;
    boolean collected;

    public Coin(double x, double y){
        this.x = x;
        this.y = y;
        setCenterX(x);
        setCenterY(y);
        setRadius(radius);
        setFill(fill);
        setStroke(Color.BLACK);
        setStrokeWidth(2);
        setStrokeType(StrokeType.INSIDE);
    }
    public void coinCollected(){
        collected = true;
    }
    public void resetCoin(){
        collected = false;
    }
}
