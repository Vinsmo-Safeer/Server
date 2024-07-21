package command;

import java.awt.*;
import java.awt.event.InputEvent;

public class ScreenController {

    public static void leftClick(int x, int y, int frameWidth, int frameHeight) {

        // Get Screen Size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        // Calculate the actual x and y
        int actualX = (int) ((double) x / frameWidth * screenWidth);
        int actualY = (int) ((double) y / frameHeight * screenHeight);


        // Create a new Robot
        try {
            Robot robot = new Robot();
            robot.mouseMove(actualX, actualY);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void rightClick(int x, int y, int frameWidth, int frameHeight) {

        // Get Screen Size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        // Calculate the actual x and y
        int actualX = (int) ((double) x / frameWidth * screenWidth);
        int actualY = (int) ((double) y / frameHeight * screenHeight);

        // Create a new Robot
        try {
            Robot robot = new Robot();
            robot.mouseMove(actualX, actualY);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void keyPress(int keyCode) {

        try {
            Robot robot = new Robot();
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }
}
