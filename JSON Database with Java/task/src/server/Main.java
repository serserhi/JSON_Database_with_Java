package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    private static final int PORT = 23456;
    private static String[] database = new String[1000];

    public static void main(String[] args) {
        //System.out.println("Hello, world!");
        System.out.println("Server started!");

        try (ServerSocket server = new ServerSocket(PORT)) {
            String receivedMsg = "";
            while (!receivedMsg.equals("exit")) {
                try (
                        Socket socket = server.accept(); // accept a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    receivedMsg = input.readUTF(); // read a message from the client
                    //System.out.println("Received: " + receivedMsg);
                    manageMessage(receivedMsg);
                    //String sentMsg = "A record # 12 was sent!";
                    output.writeUTF(manageMessage(receivedMsg)); // resend it to the client
                    //System.out.println("Sent: " + sentMsg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String manageMessage(String receivedMsg) {

        String[] parameters = receivedMsg.split(" ");
        switch (parameters[0]) {
            case "set":
                String entrada = "";
                if (Integer.parseInt(parameters[1]) > 999) {
                    return "ERROR";
                } else {
                    int i = 2;
                    while (i < parameters.length) {
                        entrada = entrada.concat(parameters[i]).concat(" ");
                        i++;
                    }
                    database[Integer.parseInt(parameters[1])] = entrada;
                    return "OK";
                }
            case "get":
                if (Integer.parseInt(parameters[1]) > 999 || database[Integer.parseInt(parameters[1])] == null) {
                    return "ERROR";
                } else {
                    return database[Integer.parseInt(parameters[1])];
                }
            case "delete":
                if (Integer.parseInt(parameters[1]) > 999 || Integer.parseInt(parameters[1]) <= 0) {
                    return "ERROR";
                } else {
                    database[Integer.parseInt(parameters[1])] = null;
                    return "OK";
                }
            default:
                return "ERROR";
        }

    }

}
