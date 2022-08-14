package impossibleGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;


public class CircleCirc extends javafx.scene.shape.Circle {
    double x;
    double y;
    double angularV;
    double radius = 7;
    Color fill  = Color.BLUEVIOLET;
    double centerX;
    double centerY;
    double pathRadius;


    public CircleCirc(double x, double y, double angularV, double centerX, double centerY, double pathRadius){
        this.x = x;
        this.y = y;
        setCenterX(x);
        setCenterY(y);
        setRadius(radius);
        setFill(fill);
        setStroke(Color.BLACK);
        setStrokeWidth(2);
        setStrokeType(StrokeType.INSIDE);
        this.angularV = angularV;
        this.centerX = centerX;
        this.centerY = centerY;
        this.pathRadius = pathRadius;
    }
    public void drawCircleCirc(){
        double angle = Math.atan2(y - centerY, x - centerX);
        angle += angularV;
        double x = Math.cos(angle) * pathRadius + centerX;
        double y = Math.sin(angle) * pathRadius + centerY;
        this.x = x;
        this.y = y;
        setCenterX(x);
        setCenterY(y);
    }
}
