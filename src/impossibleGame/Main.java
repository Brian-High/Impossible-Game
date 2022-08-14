package impossibleGame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.*;


public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    StackPane stackPane;
    GraphicsContext gc;
    Rectangle startMenuBackground;
    Rectangle endScreen;
    VBox startMenuVbox;
    VBox lvlSelectVbox;
    VBox mainVbox;
    Pane playField;
    Square square;

    StackPane topBar;
    HBox topBarHbox;
    Label deathCounter;
    Button menuBtn;
    int counter = 0;

    AnimationTimer timer;
    TextField nameFld;

    HBox hBox;
    HBox hBox2;

    VBox endGameVbox;
    HashMap<String, Double> highScores;
    boolean showScores;
    boolean startedFromBeginning = false;

    ArrayList<Wall> walls;
    ArrayList<Circle> circles;
    ArrayList<WinArea> winAreas;
    ArrayList<Coin> coins;
    ArrayList<CircleCirc> circleCircs;
    ArrayList<Button> buttons;

    boolean up = false;
    boolean down = false;
    boolean left = false;
    boolean right = false;
    boolean lvlEditor = false;
    boolean shiftHeld;
    Label shiftHeldLbl;
    TextField exportFld;
    TextField loadLvlFld;


    int level = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        mainVbox = new VBox();
        stackPane = new StackPane();
        playField = new Pane();

        topBar = new StackPane();
        topBarHbox = new HBox();
        startMenuBackground = new Rectangle(primaryStage.getWidth(),
                primaryStage.getHeight(), Color.LIGHTGREY);
        endScreen = new Rectangle(primaryStage.getWidth(),
                primaryStage.getHeight(), Color.BLACK);
        startMenuVbox = new VBox();
        lvlSelectVbox = new VBox();
        lvlSelectVbox.setAlignment(Pos.CENTER);
        Button startBtn = new Button("Start Game");
        Button lvlSelect = new Button("Level Select");
        Button lvlEditorBtn = new Button("Level Editor");
        startMenuVbox.getChildren().addAll(startBtn, lvlSelect, lvlEditorBtn);
        startMenuVbox.setAlignment(Pos.CENTER);

        Rectangle rectangle = new Rectangle(0, 0, 500, 50);
        rectangle.setFill(Color.BLACK);
        menuBtn = new Button("Menu");
        menuBtn.setStyle("fx-background-color: #000000");
        deathCounter = new Label("Death Count: " + counter);
        deathCounter.setTextFill(Color.WHITE);
        topBarHbox.getChildren().addAll(menuBtn, deathCounter);
        topBarHbox.setAlignment(Pos.CENTER);
        topBar.getChildren().addAll(rectangle, topBarHbox);

        //lvl editor
        hBox = new HBox();
        hBox2 = new HBox();

        Button wallBtn = new Button("New Wall");
        Button winAreaBtn = new Button("New Win Area");
        Button circleBtn = new Button("New Circle");
        Button squareBtn = new Button("New Square");
        Button coinBtn = new Button("New Coin");
        Button clearBtn = new Button("Clear Selection");
        Button loadLvlBtn = new Button("Load Level");
        Button exportBtn = new Button("Export");
        ToggleButton runPause = new ToggleButton("Run");
        shiftHeldLbl = new Label();
        exportFld = new TextField();
        exportFld.setMaxWidth(35);
        loadLvlFld = new TextField();
        loadLvlFld.setMaxWidth(35);
        hBox.getChildren().addAll(wallBtn, winAreaBtn, circleBtn, squareBtn, coinBtn);
        hBox2.getChildren().addAll(runPause, shiftHeldLbl, clearBtn, loadLvlFld, loadLvlBtn, exportBtn, exportFld);
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox2.setAlignment(Pos.BASELINE_CENTER);

        Canvas canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());
        gc = canvas.getGraphicsContext2D();
        mainVbox.getChildren().addAll(topBar, playField);
        stackPane.getChildren().addAll(mainVbox, startMenuBackground, lvlSelectVbox, startMenuVbox);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (up) square.drawSquare(square.x, square.y - square.vy, walls);
                if (down) square.drawSquare(square.x, square.y + square.vy, walls);
                if (left) square.drawSquare(square.x - square.vx, square.y, walls);
                if (right) square.drawSquare(square.x + square.vx, square.y, walls);
                square.checkCollisionWinArea(winAreas, coins, Main.this);
                square.checkCoinCollision(coins, Main.this);
                if (circles != null) {
                    for (Circle circle : circles) {
                        circle.drawCircle(walls);
                        square.checkCollisionCircle(circles, coins, Main.this);
                    }
                }
                if (circleCircs != null) {
                    for (CircleCirc circleCirc : circleCircs) {
                        circleCirc.drawCircleCirc();
                        square.checkCollisionCircleCirc(circleCircs, coins, Main.this);
                    }
                }
            }
        };

        startBtn.setOnAction((event) -> {
            playSound("resources/sounds/themeSong.mp3");
            lvlEditor = false;
            hBox.setVisible(false);
            hBox2.setVisible(false);
            closeStartMenu();
            Parser.runParser(level, Main.this);
            mainVbox.toFront();
            timer.start();
        });

        lvlSelect.setOnAction((event) -> {
            loadLevelMenu();
        });

        lvlEditorBtn.setOnAction((event) -> {
            closeStartMenu();
            loadLevelEditor();
        });

        ArrayList<Double> cords = new ArrayList<>();

        wallBtn.setOnAction((event) -> {
            boolean vert = false;
            boolean horz = false;
            double xLength = cords.get(2) - cords.get(0);
            double yHeight = cords.get(3) - cords.get(1);
            boolean invisible = false;
            System.out.println("cors.size" + cords.size());
            if (cords.size() > 4) {
                if (cords.get(1) > cords.get(5)) invisible = true;
            }
            System.out.println("invisible = " + invisible);
            if (xLength > yHeight) horz = true;
            if (yHeight > xLength) vert = true;
            if (horz) {
                Wall w = new Wall(cords.get(0), cords.get(1), cords.get(2) - cords.get(0), 5, invisible);
                walls.add(w);
                if (!invisible) playField.getChildren().add(w);
            }
            if (vert) {
                Wall w = new Wall(cords.get(0), cords.get(1), 5, cords.get(3) - cords.get(1), invisible);
                walls.add(w);
                if (!invisible) playField.getChildren().add(w);
            }
            cords.clear();
        });

        circleBtn.setOnAction((event) -> {
            if (cords.size() < 5) {
                System.out.println("linear path");
                double vx = 3;
                double vy = 3;
                double xLength = cords.get(2) - cords.get(0);
                double yHeight = cords.get(3) - cords.get(1);
                if (Math.abs(xLength) > Math.abs(yHeight)) {
                    vy = 0;
                    vx *= xLength / Math.abs(xLength);
                }
                if (Math.abs(xLength) < Math.abs(yHeight)) {
                    vx = 0;
                    vy *= yHeight / Math.abs(yHeight);
                }
                System.out.println("vx = " + vx);
                System.out.println("vy = " + vy);
                Circle c = new Circle(cords.get(0), cords.get(1), vx, vy);
                circles.add(c);
                playField.getChildren().add(c);
            } else {
                System.out.println("circle path");
                boolean vert = false;
                boolean horz = false;
                double xLength = cords.get(2) - cords.get(0);
                double yHeight = cords.get(3) - cords.get(1);
                if (xLength > yHeight) horz = true;
                if (yHeight > xLength) vert = true;
                double angularV = Math.PI / 50;
                double centerX = cords.get(0);
                double centerY = cords.get(1);
                double x = cords.get(2);
                double y = cords.get(3);
                if (horz) {
                    CircleCirc c = new CircleCirc(centerX, y, angularV, centerX, centerY, Math.abs(y - centerY));
                    circleCircs.add(c);
                    playField.getChildren().add(c);
                }
                if (vert) {
                    CircleCirc c = new CircleCirc(x, centerY, angularV, centerX, centerY, Math.abs(x - centerX));
                    circleCircs.add(c);
                    playField.getChildren().add(c);
                }
            }
            cords.clear();
        });

        winAreaBtn.setOnAction((event) -> {
            boolean isSpawnReset = false;
            boolean isWin = false;
            if (cords.get(5) < cords.get(0)) {
                isSpawnReset = true;
            }
            System.out.println("isSpawnReset = " + isSpawnReset);
            if (cords.get(7) < cords.get(0)) {
                isWin = true;
            }
            System.out.println("isWin = " + isWin);
            WinArea w = new WinArea(cords.get(0), cords.get(1), cords.get(2) - cords.get(0),
                    cords.get(3) - cords.get(1), isSpawnReset, isWin);
            winAreas.add(w);
            playField.getChildren().add(w);
            cords.clear();
        });

        squareBtn.setOnAction((event) -> {
            square = new Square(cords.get(0), cords.get(1), Main.this);
            playField.getChildren().add(square);
            cords.clear();
        });

        coinBtn.setOnAction((event) -> {
            Coin c = new Coin(cords.get(0), cords.get(1));
            coins.add(c);
            playField.getChildren().add(c);
            cords.clear();
        });

        primaryStage.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getSceneY() > 50) {
                    System.out.println(event.getX() + ", " + (event.getY() - 50));
                    cords.add((double) (5 * (Math.round(event.getX() / 5))));
                    cords.add((double) (5 * (Math.round(event.getY() / 5))) - 50);

                }
            }
        });

        clearBtn.setOnAction((event) -> {
            clearItem(cords);
            cords.clear();
        });

        exportBtn.setOnAction((event) -> {
            Exporter.runExporter(Main.this);
        });

        shiftHeld = false;
        showScores = false;

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getText().contentEquals("c")) {
                    cords.clear();
                }
                if (event.getCode() == KeyCode.SHIFT) {
                    shiftHeld = !shiftHeld;
                    if (shiftHeld) {
                        shiftHeldLbl.setText("Shift Held");
                    } else shiftHeldLbl.setText("");
                }
                if (lvlEditor) {
                    if (event.getText().contentEquals("w")) {
                        moveItem(cords, KeyCode.KP_UP, shiftHeld);
                        cords.clear();
                    }
                    if (event.getText().contentEquals("a")) {
                        moveItem(cords, KeyCode.KP_LEFT, shiftHeld);
                        cords.clear();
                    }
                    if (event.getText().contentEquals("d")) {
                        moveItem(cords, KeyCode.KP_RIGHT, shiftHeld);
                        cords.clear();
                    }
                    if (event.getText().contentEquals("s")) {
                        moveItem(cords, KeyCode.KP_DOWN, shiftHeld);
                        cords.clear();
                    }
                } else {
                    if (event.getText().contentEquals("w")) up = true;
                    if (event.getText().contentEquals("a")) left = true;
                    if (event.getText().contentEquals("s")) down = true;
                    if (event.getText().contentEquals("d")) right = true;
                }
                System.out.println(showScores);
                if (showScores ) {
                    if (event.getCode() == KeyCode.ENTER) {
                        Exporter.exportScores(Main.this);
                    }
                }
            }
        });
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getText().contentEquals("w")) up = false;
                if (event.getText().contentEquals("a")) left = false;
                if (event.getText().contentEquals("s")) down = false;
                if (event.getText().contentEquals("d")) right = false;
            }
        });

        loadLvlBtn.setOnAction((event) -> {
            clearPlayField();
            Canvas c = new Canvas();
            c.setWidth(500);
            c.setHeight(500);
            gc = c.getGraphicsContext2D();
            playField.getChildren().add(c);
            for (int i = 0; i < 500; i += 10) {
                for (int j = 0; j < 500; j += 10) {
                    gc.setStroke(Color.color(.5, .5, .5));
                    gc.strokeLine(i, j, 500, j);
                    gc.strokeLine(i, j, i, 500);
                }
            }
            Parser.runParser(Integer.parseInt(loadLvlFld.getText()), Main.this);
            exportFld.setText(String.valueOf(Integer.parseInt(loadLvlFld.getText()) + 1));
        });

        menuBtn.setOnAction((event) -> {
            timer.stop();
            loadMenu();
        });

        runPause.setOnAction((event) -> {
            if (runPause.isSelected()) {
                timer.start();
                runPause.setText("Pause");
            } else {
                timer.stop();
                runPause.setText("Run");
            }
        });

        Scene root = new Scene(stackPane);
        root.setCursor(Cursor.CROSSHAIR);
        primaryStage.setScene(root);
        primaryStage.setTitle("The IMPOSSIBLE Game");
        primaryStage.show();
    }

    public void closeStartMenu() {
        startMenuBackground.setVisible(false);
        startMenuVbox.setVisible(false);
    }

    public void loadMenu() {
        startMenuBackground.toFront();
        startMenuVbox.toFront();
        counter = 0;
        deathCounter.setText("Death Counter: " + counter);
        clearPlayField();
        startMenuBackground.setVisible(true);
        startMenuVbox.setVisible(true);
        mainVbox.getChildren().removeAll(hBox,hBox2);
    }

    public void loadLevelMenu() {
        lvlSelectVbox.setVisible(true);
        closeStartMenu();
        lvlEditor = false;
        counter = 0;
        deathCounter.setText("Death Counter: " + counter);
        startMenuBackground.setVisible(true);
        String strLevelsDir = "resources/levels";
        File levelsDir = new File(strLevelsDir);
        File[] levelsFiles = levelsDir.listFiles();
        ArrayList<File> lvlFiles = new ArrayList<>();
        lvlSelectVbox.toFront();
        if (buttons == null) {
            buttons = new ArrayList<>();
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
                Button b = new Button(file.getName());
                buttons.add(b);
                lvlSelectVbox.getChildren().add(b);
            }
            buttons.remove(buttons.size()-1);
            lvlSelectVbox.getChildren().remove(lvlSelectVbox.getChildren().size()-1);

            for (Button b : buttons) {
                b.setOnAction((event) -> {
                    String name = b.getText();
                    int index = 0;
                    if (name.charAt(1) == '.') {
                        String s = String.valueOf(name.charAt(0));
                        index = Integer.valueOf(s) - 1;
                    } else if (name.charAt(2) == '.') {
                        String s = String.valueOf(name.charAt(0));
                        s += String.valueOf(name.charAt(1));
                        index = Integer.valueOf(s) - 1;
                    }
                    level = index;
                    Parser.runParser(index, Main.this);
                    startMenuBackground.setVisible(false);
                    lvlSelectVbox.setVisible(false);
                    hBox.setVisible(false);
                    hBox2.setVisible(false);
                    timer.start();
                });
            }
        }
    }

    public void loadNextLevel() {
        clearPlayField();
        level++;
        Parser.runParser(level, Main.this);
        mainVbox.toFront();
    }

    public void clearPlayField() {
        playField.getChildren().clear();
        walls.clear();
        if (circles != null) {
            circles.clear();
        }
        if (circleCircs != null) {
            circleCircs.clear();
        }
        winAreas.clear();

        if (coins != null) {
            coins.clear();
        }
    }

    public void endGame() {
        Label congrats = new Label("Congratulations you won!");
        Label highScrLbl = new Label("Enter your Name");
        nameFld = new TextField();
        endGameVbox = new VBox();
        endGameVbox.getChildren().addAll(congrats);
        if(startedFromBeginning) {
            endGameVbox.getChildren().addAll(highScrLbl, nameFld);
        }
        startMenuBackground.setVisible(true);
        playField.getChildren().addAll(startMenuBackground, endGameVbox);
    }

    public void highScoreScreen() {
        endGameVbox.getChildren().clear();
        ArrayList<Label> scores = new ArrayList<>();
        highScores.put(nameFld.getText(), (double) counter);
        highScores = sortByValues(highScores);
        Label label = new Label("High Scores");
        scores.add(label);
        highScores.forEach((name, score) -> {
            Label l = new Label(name + " " + score);
            scores.add(l);
        });
        for (Label score : scores) {
            endGameVbox.getChildren().add(score);
        }

    }

    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public void loadLevelEditor() {
        lvlEditor = true;
        hBox.setVisible(true);
        hBox2.setVisible(true);
        closeStartMenu();
        mainVbox.getChildren().addAll(hBox, hBox2);
        mainVbox.toFront();
        winAreas = new ArrayList<>();
        circles = new ArrayList<>();
        circleCircs = new ArrayList<>();
        walls = new ArrayList<>();
        coins = new ArrayList<>();
        Canvas c = new Canvas();
        c.setWidth(500);
        c.setHeight(500);
        gc = c.getGraphicsContext2D();
        playField.getChildren().add(c);
        for (int i = 0; i < 500; i += 10) {
            for (int j = 0; j < 500; j += 10) {
                gc.setStroke(Color.color(.5, .5, .5));
                gc.strokeLine(i, j, 500, j);
                gc.strokeLine(i, j, i, 500);
            }
        }
    }

    public void clearItem(ArrayList<Double> cords) {
        if (walls != null) {
            for (Wall wall : walls) {
                double wTop = wall.y;
                double wBot = wall.y + wall.height;
                double wLeft = wall.x;
                double wRight = wall.x + wall.width;
                double xOverlap = Math.min(cords.get(0), wRight) - Math.max(cords.get(0), wLeft);
                double yOverlap = Math.min(cords.get(1), wBot) - Math.max(cords.get(1), wTop);
                if (xOverlap == 0 && yOverlap == 0) {
                    walls.remove(wall);
                    playField.getChildren().remove(wall);
                    break;
                }
            }
        }
        if (circles != null) {
            for (Circle circle : circles) {
                double wTop = circle.y - circle.radius;
                double wBot = circle.y + circle.radius;
                double wLeft = circle.x - circle.radius;
                double wRight = circle.x + circle.radius;
                double xOverlap = Math.min(cords.get(0), wRight) - Math.max(cords.get(0), wLeft);
                double yOverlap = Math.min(cords.get(1), wBot) - Math.max(cords.get(1), wTop);
                if (xOverlap == 0 && yOverlap == 0) {
                    circles.remove(circle);
                    playField.getChildren().remove(circle);
                    break;
                }
            }
        }
        if (circleCircs != null) {
            for (CircleCirc circleCirc : circleCircs) {
                double wTop = circleCirc.y - circleCirc.radius;
                double wBot = circleCirc.y + circleCirc.radius;
                double wLeft = circleCirc.x - circleCirc.radius;
                double wRight = circleCirc.x + circleCirc.radius;
                double xOverlap = Math.min(cords.get(0), wRight) - Math.max(cords.get(0), wLeft);
                double yOverlap = Math.min(cords.get(1), wBot) - Math.max(cords.get(1), wTop);
                if (xOverlap == 0 && yOverlap == 0) {
                    circleCircs.remove(circleCirc);
                    playField.getChildren().remove(circleCirc);
                    break;
                }
            }
        }
        if (coins != null) {
            for (Coin coin : coins) {
                double wTop = coin.y - coin.radius;
                double wBot = coin.y + coin.radius;
                double wLeft = coin.x - coin.radius;
                double wRight = coin.x + coin.radius;
                double xOverlap = Math.min(cords.get(0), wRight) - Math.max(cords.get(0), wLeft);
                double yOverlap = Math.min(cords.get(1), wBot) - Math.max(cords.get(1), wTop);
                if (xOverlap == 0 && yOverlap == 0) {
                    coins.remove(coin);
                    playField.getChildren().remove(coin);
                    break;
                }
            }
        }
        if (winAreas != null) {
            for (WinArea winArea : winAreas) {
                double wTop = winArea.y;
                double wBot = winArea.y + winArea.height;
                double wLeft = winArea.x;
                double wRight = winArea.x + winArea.width;
                double xOverlap = Math.min(cords.get(0), wRight) - Math.max(cords.get(0), wLeft);
                double yOverlap = Math.min(cords.get(1), wBot) - Math.max(cords.get(1), wTop);
                if (xOverlap == 0 && yOverlap == 0) {
                    winAreas.remove(winArea);
                    playField.getChildren().remove(winArea);
                    break;
                }
            }
        }
    }

    public void moveItem(ArrayList<Double> cords, KeyCode keyCode, boolean shiftHeld) {
        System.out.println(keyCode);
        if (walls != null) {
            for (Wall wall : walls) {
                double wTop = wall.y;
                double wBot = wall.y + wall.height;
                double wLeft = wall.x;
                double wRight = wall.x + wall.width;
                double xOverlap = Math.min(cords.get(0), wRight) - Math.max(cords.get(0), wLeft);
                double yOverlap = Math.min(cords.get(1), wBot) - Math.max(cords.get(1), wTop);
                if (xOverlap == 0 && yOverlap == 0) {
                    if (!shiftHeld) {
                        if (keyCode == KeyCode.KP_UP) {
                            wall.setY(wall.y - 5);
                            wall.y -= 5;
                        }
                        if (keyCode == KeyCode.KP_RIGHT) {
                            wall.setX(wall.x + 5);
                            wall.x += 5;
                        }
                        if (keyCode == KeyCode.KP_LEFT) {
                            wall.setX(wall.x - 5);
                            wall.x -= 5;
                        }
                        if (keyCode == KeyCode.KP_DOWN) {
                            wall.setY(wall.y + 5);
                            wall.y += 5;
                        }
                    } else if (shiftHeld) {
                        if (keyCode == KeyCode.KP_UP) {
                            wall.setY(wall.y - 5);
                            wall.y -= 5;
                            wall.setHeight(wall.height + 5);
                            wall.height += 5;
                        }
                        if (keyCode == KeyCode.KP_RIGHT) {
                            wall.setWidth(wall.width + 5);
                            wall.width += 5;
                        }
                        if (keyCode == KeyCode.KP_LEFT) {
                            wall.setX(wall.x - 5);
                            wall.x -= 5;
                            wall.setWidth(wall.width + 5);
                            wall.width += 5;
                        }
                        if (keyCode == KeyCode.KP_DOWN) {
                            wall.setHeight(wall.height + 5);
                            wall.height += 5;
                        }
                    }
                    break;
                }
            }
        }
        if (circles != null) {
            for (Circle circle : circles) {
                double wTop = circle.y - circle.radius;
                double wBot = circle.y + circle.radius;
                double wLeft = circle.x - circle.radius;
                double wRight = circle.x + circle.radius;
                double xOverlap = Math.min(cords.get(0), wRight) - Math.max(cords.get(0), wLeft);
                double yOverlap = Math.min(cords.get(1), wBot) - Math.max(cords.get(1), wTop);
                if (xOverlap == 0 && yOverlap == 0) {
                    if (keyCode == KeyCode.KP_UP) {
                        circle.setCenterY(circle.y - 5);
                        circle.y -= 5;
                    }
                    if (keyCode == KeyCode.KP_RIGHT) {
                        circle.setCenterX(circle.x + 5);
                        circle.x += 5;
                    }
                    if (keyCode == KeyCode.KP_LEFT) {
                        circle.setCenterX(circle.x - 5);
                        circle.x -= 5;
                    }
                    if (keyCode == KeyCode.KP_DOWN) {
                        circle.setCenterY(circle.y + 5);
                        circle.y += 5;
                    }
                    break;
                }
            }
        }
        if (coins != null) {
            for (Coin coin : coins) {
                double wTop = coin.y - coin.radius;
                double wBot = coin.y + coin.radius;
                double wLeft = coin.x - coin.radius;
                double wRight = coin.x + coin.radius;
                double xOverlap = Math.min(cords.get(0), wRight) - Math.max(cords.get(0), wLeft);
                double yOverlap = Math.min(cords.get(1), wBot) - Math.max(cords.get(1), wTop);
                if (xOverlap == 0 && yOverlap == 0) {
                    if (keyCode == KeyCode.KP_UP) {
                        coin.setCenterY(coin.y - 5);
                        coin.y -= 5;
                    }
                    if (keyCode == KeyCode.KP_RIGHT) {
                        coin.setCenterX(coin.x + 5);
                        coin.x += 5;
                    }
                    if (keyCode == KeyCode.KP_LEFT) {
                        coin.setCenterX(coin.x - 5);
                        coin.x -= 5;
                    }
                    if (keyCode == KeyCode.KP_DOWN) {
                        coin.setCenterY(coin.y + 5);
                        coin.y += 5;
                    }
                    break;
                }
            }
        }
        if (winAreas != null) {
            for (WinArea winArea : winAreas) {
                double wTop = winArea.y;
                double wBot = winArea.y + winArea.height;
                double wLeft = winArea.x;
                double wRight = winArea.x + winArea.width;
                double xOverlap = Math.min(cords.get(0), wRight) - Math.max(cords.get(0), wLeft);
                double yOverlap = Math.min(cords.get(1), wBot) - Math.max(cords.get(1), wTop);
                if (xOverlap == 0 && yOverlap == 0) {
                    if (!shiftHeld) {
                        if (keyCode == KeyCode.KP_UP) {
                            winArea.setY(winArea.y - 5);
                            winArea.y -= 5;
                        }
                        if (keyCode == KeyCode.KP_RIGHT) {
                            winArea.setX(winArea.x + 5);
                            winArea.x += 5;
                        }
                        if (keyCode == KeyCode.KP_LEFT) {
                            winArea.setX(winArea.x - 5);
                            winArea.x -= 5;
                        }
                        if (keyCode == KeyCode.KP_DOWN) {
                            winArea.setY(winArea.y + 5);
                            winArea.y += 5;
                        }
                    } else {
                        if (keyCode == KeyCode.KP_UP) {
                            winArea.setY(winArea.y - 5);
                            winArea.setHeight(winArea.height + 5);
                            winArea.height += 5;
                            winArea.y -= 5;
                        }
                        if (keyCode == KeyCode.KP_RIGHT) {
                            winArea.setWidth(winArea.width + 5);
                            winArea.width += 5;
                        }
                        if (keyCode == KeyCode.KP_LEFT) {
                            winArea.setX(winArea.x - 5);
                            winArea.setWidth(winArea.width + 5);
                            winArea.x -= 5;
                            winArea.width += 5;
                        }
                        if (keyCode == KeyCode.KP_DOWN) {
                            winArea.setHeight(winArea.height + 5);
                            winArea.height += 5;
                        }
                    }
                    break;
                }
            }
        }
    }
    public void playSound(String fileName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println(fileName);
            System.out.println("ex = " + ex);
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

}
