package command;

import main.Client;
import main.Info;

import java.io.File;

public class Delete {
    public static void delete(String command) {
        // delete <file_name>

        String fileName = command.substring(7);
        String path = Info.SERVER_FOLDER + "\\" + fileName;

        System.out.println("Deleting file: " + fileName);

        // Delete file
        File file = new File(path);
        if (file.delete()) {
            System.out.println("File deleted successfully");
            Client.send("File deleted successfully", Info.clientIP, 54321, true);
        } else {
            System.out.println("Failed to delete the file");
            Client.send("Failed to delete the file", Info.clientIP, 54321, true);
        }
    }
}
