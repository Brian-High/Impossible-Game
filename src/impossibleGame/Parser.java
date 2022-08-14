package impossibleGame;

import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {
    public static void runParser(int level, Main main){
        String strLevelsDir = "resources/levels";
        File levelsDir = new File(strLevelsDir);
        File[] levelsFiles = levelsDir.listFiles();
        ArrayList<File> lvlFiles = new ArrayList<>();
        //sort files
        {
            for (int i = 0; i < levelsFiles.length; i++) {
                File f = new File("");
                lvlFiles.add(f);
            }
            for (File file : levelsFiles) {
                String name = file.getName();
                if (!name.contentEquals(".DS_Store")) {
                    if (name.charAt(1) == '.') {
                        String s = String.valueOf(name.charAt(0));
                        int index = Integer.valueOf(s) - 1;
                        lvlFiles.remove(index);
                        lvlFiles.add(index, file);
                    } else if (name.charAt(2) == '.') {
                        String s = String.valueOf(name.charAt(0));
                        s += String.valueOf(name.charAt(1));
                        int index = Integer.valueOf(s) - 1;
                        lvlFiles.remove(index);
                        lvlFiles.add(index, file);
                    }
                }
            }
            for (int i = 0; i < lvlFiles.size(); i++) {
                levelsFiles[i] = lvlFiles.get(i);
            }
            for (File file : levelsFiles) {
                System.out.println(file.getName());
            }
        }

        if (levelsFiles != null) {
            try {
                Scanner scanner;
                if(level > levelsFiles.length-2){
                    File highScores = new File("resources/highScores/highScores");
                    scanner = new Scanner(highScores);
                } else {
                    scanner = new Scanner(levelsFiles[level]);
                }
                if(level == 1){
                    main.startedFromBeginning = true;
                }
                System.out.println("file Name " + levelsFiles[level].getName());
                String fileContents = "";
                while (scanner.hasNext()) {
                    fileContents += scanner.next();
                }
                System.out.println("fileContents= " + fileContents);
                String[] splitContents = fileContents.split("!");
                for (String string : splitContents) {
                    System.out.println("string = " + string);
                }
                for (String string : splitContents) {
                    Scanner scanner1 = new Scanner(string);
                    scanner1.useDelimiter(":");
                    String type = scanner1.next();
                    System.out.println("type = " + type);
                    switch (type) {
                        case "walls":
                            String wallCords = scanner1.next();
                            System.out.println("wallCords = " + wallCords);
                            drawWalls(wallCords, main);
                            break;
                        case "circles":
                            String circleCords = scanner1.next();
                            System.out.println("circleCords = " + circleCords);
                            drawCircles(circleCords, main);
                            break;
                        case "circleCircs":
                            String circleCircCords = scanner1.next();
                            System.out.println("circleCircCords = " + circleCircCords);
                            drawCircleCircs(circleCircCords, main);
                            break;
                        case "winAreas":
                            String winAreaCords = scanner1.next();
                            System.out.println("winAreaCords = " + winAreaCords);
                            drawWinAreas(winAreaCords, main);
                            break;
                        case "square":
                            String squareCords = scanner1.next();
                            System.out.println("squareCords = " + squareCords);
                            drawSquares(squareCords, main);
                            break;
                        case "coins":
                            String coinsCords = scanner1.next();
                            System.out.println("coinsCords = " + coinsCords);
                            drawCoins(coinsCords, main);
                            break;
                        case "end":
                            String endStr = scanner1.next();
                            System.out.println("endStr = " + endStr);
                            drawEnd(endStr, main);
                            break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void drawWalls(String wallCords, Main main) {
        main.walls = new ArrayList<>();

        String[] walls = wallCords.split("[$]");
        for (String wall : walls) {
            Scanner scanner = new Scanner(wall);
            scanner.useDelimiter("[,]");
            double x = Double.parseDouble(scanner.next());
            double y = Double.parseDouble(scanner.next());
            double w = Double.parseDouble(scanner.next());
            double h = Double.parseDouble(scanner.next());
            boolean b = Boolean.parseBoolean(scanner.next());
            main.walls.add(new Wall(x, y, w, h, b));
        }
        for (Wall wall : main.walls) {
            if(!wall.invisible) main.playField.getChildren().add(wall);
        }

    }

    public static void drawCircles(String circleCords, Main main) {
        main.circles = new ArrayList<>();

        String[] circles = circleCords.split("[$]");
        for (String circle : circles) {
            Scanner scanner = new Scanner(circle);
            scanner.useDelimiter("[,]");
            double x = Double.parseDouble(scanner.next());
            double y = Double.parseDouble(scanner.next());
            double vx = Double.parseDouble(scanner.next());
            double vy = Double.parseDouble(scanner.next());
            main.circles.add(new Circle(x, y, vx, vy));
        }
        for (Circle circle : main.circles) {
            main.playField.getChildren().add(circle);
        }
    }
    public static void drawCircleCircs(String circleCircCords, Main main) {
        main.circleCircs = new ArrayList<>();

        String[] circleCircs = circleCircCords.split("[$]");
        for (String circleCirc : circleCircs) {
            Scanner scanner = new Scanner(circleCirc);
            scanner.useDelimiter("[,]");
            double x = Double.parseDouble(scanner.next());
            double y = Double.parseDouble(scanner.next());
            double angularV = Double.parseDouble(scanner.next());
            double centerX = Double.parseDouble(scanner.next());
            double centerY = Double.parseDouble(scanner.next());
            double pathRadius = Double.parseDouble(scanner.next());
            main.circleCircs.add(new CircleCirc(x, y, angularV, centerX, centerY, pathRadius));
        }
        for (CircleCirc circleCirc : main.circleCircs) {
            main.playField.getChildren().add(circleCirc);
        }
    }

    public static void drawWinAreas(String winAreaCords, Main main) {
        main.winAreas = new ArrayList<>();
        String[] winAreas = winAreaCords.split("[$]");
        for (String winArea : winAreas) {
            Scanner scanner = new Scanner(winArea);
            scanner.useDelimiter("[,]");
            double x = Double.parseDouble(scanner.next());
            double y = Double.parseDouble(scanner.next());
            double w = Double.parseDouble(scanner.next());
            double h = Double.parseDouble(scanner.next());
            boolean isWin = Boolean.parseBoolean(scanner.next());
            boolean isSpawnReset = Boolean.parseBoolean(scanner.next());
            main.winAreas.add(new WinArea(x, y, w, h, isWin, isSpawnReset));
        }
        for (WinArea winArea : main.winAreas) {
            main.playField.getChildren().add(winArea);
        }
    }

    public static void drawSquares(String squareCords, Main main) {
        String[] squares = squareCords.split("[$]");
        for (String square : squares) {
            Scanner scanner = new Scanner(square);
            scanner.useDelimiter("[,]");
            double x = Double.parseDouble(scanner.next());
            double y = Double.parseDouble(scanner.next());
            main.square = new Square(x, y, main);
            main.playField.getChildren().add(main.square);
        }
    }

    public static void drawCoins(String coinsCords, Main main) {
        main.coins = new ArrayList<>();

        String[] coins = coinsCords.split("[$]");
        for (String coin : coins) {
            Scanner scanner = new Scanner(coin);
            scanner.useDelimiter("[,]");
            double x = Double.parseDouble(scanner.next());
            double y = Double.parseDouble(scanner.next());
            main.coins.add(new Coin(x, y));
        }
        for (Coin coin : main.coins) {
            main.playField.getChildren().add(coin);
        }
        if(main.coins.size() == 0) main.coins = null;
    }

    public static void drawEnd(String endStr, Main main){
        main.clearPlayField();
        main.showScores = true;
        main.endGame();
        String[] highScores = endStr.split("[$]");
        HashMap<String,Double> highScoresMap = new HashMap<>();
        for(String score: highScores){
            Scanner scanner = new Scanner(score);
            scanner.useDelimiter(",");
            highScoresMap.put(scanner.next(), Double.parseDouble(scanner.next()));
        }
        main.highScores = highScoresMap;

    }
}
