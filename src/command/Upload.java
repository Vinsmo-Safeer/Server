package command;

import main.Client;
import main.Info;

public class Upload {
    public static void upload(String command) {
        // upload <file_in_client> <folder_in_server>

        // If there is no <folder_in_server> specified, use the server folder
        String[] commandParts = command.split(" ");
        String fileName = commandParts[1].split("\\\\")[commandParts[1].split("\\\\").length - 1];
        String folderName = commandParts.length == 3 ? commandParts[2] : "default";

        if (folderName.equals("default")) {
            folderName = Info.SERVER_FOLDER;
        } else {
            folderName = Info.SERVER_FOLDER + "\\" + folderName;
        }

        Client.send("true", Info.clientIP, 54321, true);
        Client.receiveFile(folderName + "\\" + fileName, 12121);
    }
}
