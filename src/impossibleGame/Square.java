package impossibleGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;

public class Square extends Rectangle {
    double startx;
    double starty;
    double x;
    double y;
    double vx = 2.5;
    double vy = 2.5;
    boolean hasCoin;
    static double height = 15;
    static double width = 15;
    Color fill = Color.RED;

    public Square(double x, double y, Main main) {
        this.x = x;
        this.y = y;
        startx = x;
        starty = y;
        hasCoin = false;
        setX(x);
        setY(y);
        setFill(fill);
        setWidth(width);
        setHeight(height);
        setStrokeWidth(2);
        setStroke(Color.BLACK);
        setStrokeType(StrokeType.INSIDE);
    }

    public void drawSquare(double x, double y, ArrayList<Wall> walls) {
        setX(x);
        setY(y);
        this.x = x;
        this.y = y;
        checkCollisionWall(walls);
    }

    public void checkCollisionWall(ArrayList<Wall> walls) {
        double top = this.y;
        double bot = this.y + height;
        double left = this.x;
        double right = this.x + width;

        for (Wall wall : walls) {
            double wTop = wall.y;
            double wBot = wall.y + wall.height;
            double wLeft = wall.x;
            double wRight = wall.x + wall.width;

            double xOverlap = Math.min(right, wRight) - Math.max(left, wLeft);
            double yOverlap = Math.min(bot, wBot) - Math.max(top, wTop);

            if(!wall.invisible) {
                if (xOverlap > 0 && yOverlap > 0) {
                    if (xOverlap < yOverlap) {
                        if (x > wall.x) x = wall.x + wall.width;
                        if (x < wall.x + wall.width) x = wall.x - width;
                        drawSquare(x, y, walls);
                    }
                    if (xOverlap > yOverlap) {
                        if (y > wall.y) y = wall.y + wall.height;
                        if (y < wall.y + wall.height) y = wall.y - height;
                        drawSquare(x, y, walls);
                    }
                }
            }
        }
    }

    public void checkCollisionCircle(ArrayList<Circle> circles, ArrayList<Coin> coins, Main main) {
        double top = this.y;
        double bot = this.y + height;
        double left = this.x;
        double right = this.x + width;

        boolean inWin = false;

        for (Circle circle : circles) {
            double cTop = circle.y - circle.radius;
            double cBot = circle.y + circle.radius;
            double cLeft = circle.x - circle.radius;
            double cRight = circle.x + circle.radius;

            double xOverlap = Math.min(right, cRight) - Math.max(left, cLeft);
            double yOverlap = Math.min(bot, cBot) - Math.max(top, cTop);

            if (xOverlap > 0 && yOverlap > 0) {
                for (WinArea winArea : main.winAreas) {
                    double wTop = winArea.y;
                    double wBot = winArea.y + winArea.height;
                    double wLeft = winArea.x;
                    double wRight = winArea.x + winArea.width;

                    double xOverlap1 = Math.min(right, wRight) - Math.max(left, wLeft);
                    double yOverlap1 = Math.min(bot, wBot) - Math.max(top, wTop);

                    if (xOverlap1 > 0 && yOverlap1 > 0) {
                        inWin = true;
                    }

                }
                if(!inWin) {
                    main.playSound("resources/sounds/deathSound.wav");
                    setX(startx);
                    setY(starty);
                    x = startx;
                    y = starty;

                    if (coins != null) {
                        for (Coin coin : coins) {
                            if (coin.collected) {
                                main.playField.getChildren().add(coin);
                            }
                            coin.resetCoin();
                        }
                    }
                    main.counter++;
                    main.deathCounter.setText("Death Counter: " + main.counter);
                }
            }
        }
    }
    public void checkCollisionCircleCirc(ArrayList<CircleCirc> circleCircs, ArrayList<Coin> coins, Main main) {
        double top = this.y;
        double bot = this.y + height;
        double left = this.x;
        double right = this.x + width;

        boolean inWin = false;

        for (CircleCirc circleCirc : circleCircs) {
            double cTop = circleCirc.y - circleCirc.radius;
            double cBot = circleCirc.y + circleCirc.radius;
            double cLeft = circleCirc.x - circleCirc.radius;
            double cRight = circleCirc.x + circleCirc.radius;

            double xOverlap = Math.min(right, cRight) - Math.max(left, cLeft);
            double yOverlap = Math.min(bot, cBot) - Math.max(top, cTop);

            if (xOverlap > 0 && yOverlap > 0) {

                for (WinArea winArea : main.winAreas) {
                    double wTop = winArea.y;
                    double wBot = winArea.y + winArea.height;
                    double wLeft = winArea.x;
                    double wRight = winArea.x + winArea.width;

                    double xOverlap1 = Math.min(right, wRight) - Math.max(left, wLeft);
                    double yOverlap1 = Math.min(bot, wBot) - Math.max(top, wTop);

                    if (xOverlap1 > 0 && yOverlap1 > 0) {
                        inWin = true;
                    }

                }
                if(!inWin) {
                    main.playSound("resources/sounds/deathSound.wav");
                    setX(startx);
                    setY(starty);
                    x = startx;
                    y = starty;

                    if (coins != null) {
                        for (Coin coin : coins) {
                            if (coin.collected) {
                                main.playField.getChildren().add(coin);
                            }
                            coin.resetCoin();
                        }
                    }
                    main.counter++;
                    main.deathCounter.setText("Death Counter: " + main.counter);
                }
            }
        }
    }
    public void checkCollisionWinArea(ArrayList<WinArea> winAreas, ArrayList<Coin> coins, Main main){
        double top = this.y;
        double bot = this.y + height;
        double left = this.x;
        double right = this.x + width;

        for (WinArea winArea : winAreas) {
            double wTop = winArea.y;
            double wBot = winArea.y + winArea.height;
            double wLeft = winArea.x;
            double wRight = winArea.x + winArea.width;

            double xOverlap = Math.min(right, wRight) - Math.max(left, wLeft);
            double yOverlap = Math.min(bot, wBot) - Math.max(top, wTop);

            if (xOverlap > 0 && yOverlap > 0) {
                if(winArea.isSpawnReset){
                    startx = winArea.x + winArea.width/2 - (width/2);
                    starty = winArea.y + winArea.height/2 - (height/2);
                }
                if(winArea.isWin){
                    boolean allCoinsCollected = true;
                    if(coins != null) {
                        for (Coin coin : coins) {
                            if (!coin.collected) {
                                allCoinsCollected = false;
                            }
                        }
                    }
                    if(allCoinsCollected) main.loadNextLevel();
                }
            }
        }
    }
    public void checkCoinCollision(ArrayList<Coin> coins, Main main){
        if(coins != null) {
            double top = this.y;
            double bot = this.y + height;
            double left = this.x;
            double right = this.x + width;

            for (Coin coin : coins) {
                double cTop = coin.y - coin.radius;
                double cBot = coin.y + coin.radius;
                double cLeft = coin.x - coin.radius;
                double cRight = coin.x + coin.radius;

                double xOverlap = Math.min(right, cRight) - Math.max(left, cLeft);
                double yOverlap = Math.min(bot, cBot) - Math.max(top, cTop);

                if (xOverlap > 0 && yOverlap > 0) {
                    main.playField.getChildren().remove(coin);
                    coin.coinCollected();
                }
            }
        }
    }
}
