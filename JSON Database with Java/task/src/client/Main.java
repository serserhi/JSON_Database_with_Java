package  client;



import java.io.*;
import java.net.*;
import com.beust.jcommander.*;
import java.util.Scanner;

public class Main {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    @Parameter(names = {"--type", "-t"}, description = "Type of the request")
    private String type;

    @Parameter(names = {"--index", "-i"}, description = "Index of the cell")
    private Integer index;

    @Parameter(names = {"--message", "-m"}, description = "Value/message to save in the database (only needed for set requests)")
    private String message;

    public static void main(String[] args) {
        //System.out.println("Hello, world!");

        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.run();
    }
    public void run() {
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        ) {
            System.out.println("Client started!");
            String sentMsg = "";
            sentMsg = (message == null)? type +  ((index == null)? "": " " + index) :type + " " + index + " " + message;;
            output.writeUTF(sentMsg); // send a message to the server
            System.out.println("Sent: " + sentMsg);
            String receivedMsg = input.readUTF(); // read the reply from the server
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
