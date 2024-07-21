package command;

public class CommandHandler {

    public static void handleCommand(String command) {

        if (command.startsWith("start")) {
            Start.start(command);
        } else if (command.startsWith("delete")) {
            Delete.delete(command);
        } else if (command.startsWith("upload")) {
            Upload.upload(command);
        } else if (command.startsWith("control")) {
            Control.control(command);
        }
    }
}
