package impossibleGame;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Exporter {
    public static void runExporter(Main main) {
        File file = new File("resources/levels/" + main.exportFld.getText() + ".txt");
        try {
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            if (main.walls != null && main.walls.size() >0) {
                writer.append("walls:");
                for (Wall wall : main.walls) {
                    writer.append(String.valueOf(wall.x)).append(",");
                    writer.append(String.valueOf(wall.y)).append(",");
                    writer.append(String.valueOf(wall.width)).append(",");
                    writer.append(String.valueOf(wall.height)).append(",");
                    writer.append(String.valueOf(wall.invisible)).append(",");
                    writer.append('$');
                }
                writer.append("!");
            }
            if (main.circles != null && main.circles.size() > 0) {
                writer.append("circles:");
                for (Circle circle : main.circles) {
                    writer.append(String.valueOf(circle.x)).append(",");
                    writer.append(String.valueOf(circle.y)).append(",");
                    writer.append(String.valueOf(circle.vx)).append(",");
                    writer.append(String.valueOf(circle.vy)).append(",");
                    writer.append('$');
                }
                writer.append("!");
            }
            if (main.circleCircs != null && main.circleCircs.size() > 0) {
                writer.append("circleCircs:");
                for (CircleCirc circleCirc : main.circleCircs) {
                    writer.append(String.valueOf(circleCirc.x)).append(",");
                    writer.append(String.valueOf(circleCirc.y)).append(",");
                    writer.append(String.valueOf(circleCirc.angularV)).append(",");
                    writer.append(String.valueOf(circleCirc.centerX)).append(",");
                    writer.append(String.valueOf(circleCirc.centerY)).append(",");
                    writer.append(String.valueOf(circleCirc.pathRadius)).append(",");
                    writer.append('$');
                }
                writer.append("!");
            }
            if (main.coins != null && main.coins.size() > 0) {
                writer.append("coins:");
                for (Coin coin : main.coins) {
                    writer.append(String.valueOf(coin.x)).append(",");
                    writer.append(String.valueOf(coin.y)).append(",");
                    writer.append('$');
                }
                writer.append("!");
            }
            if (main.winAreas != null && main.winAreas.size() > 0) {
                writer.append("winAreas:");
                for (WinArea winArea : main.winAreas) {
                    writer.append(String.valueOf(winArea.x)).append(",");
                    writer.append(String.valueOf(winArea.y)).append(",");
                    writer.append(String.valueOf(winArea.width)).append(",");
                    writer.append(String.valueOf(winArea.height)).append(",");
                    writer.append(String.valueOf(winArea.isSpawnReset)).append(",");
                    writer.append(String.valueOf(winArea.isWin)).append(",");
                    writer.append('$');
                }
                writer.append("!");
            }

            if (main.square != null) {
                writer.append("square:");
                writer.append(String.valueOf(main.square.x)).append(",");
                writer.append(String.valueOf(main.square.y)).append(",");
            } else {
                writer.append("square:");
                writer.append("0,").append("0,");
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public static void exportScores(Main main){
        File highScoresFile = new File("resources/highScores/highScores");
        try {
            Scanner scanner = new Scanner(highScoresFile);
            String contents = "";
            while (scanner.hasNext()) {
                contents += scanner.next();
                System.out.println("contents = " + contents);
            }
            contents = contents.replace("!", "");
            contents += main.nameFld.getText();
            contents += "," + main.counter + "$";
            contents += "!";
            System.out.println("contents = " + contents);
            highScoresFile.createNewFile();
            FileWriter fw = new FileWriter(highScoresFile);
            fw.append(contents);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        main.highScoreScreen();
    }
}
