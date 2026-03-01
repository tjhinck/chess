package server;

import dataaccess.MemoryUserDao;

public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server(
                new MemoryUserDao());

        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}