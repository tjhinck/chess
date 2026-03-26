package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        String serverUrl;
        if (args.length > 1) {
            serverUrl = args[0];
        } else {
            serverUrl = "http://localhost:8080";
        }
        try {
            new Client(serverUrl).run();

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}
