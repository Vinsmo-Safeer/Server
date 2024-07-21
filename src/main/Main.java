package main;

public class Main {



    public static void main(String[] args) {

        boolean restart = true;
        while (restart) {
            try {


                if (Info.clientIP == null) {
                    Info.clientIP = Connect.connect();
                }

                Connect.checkConnection();

                Client.receiveCommand(12345);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
