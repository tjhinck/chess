package server;

import dataaccess.MemoryAuthDao;
import dataaccess.MemoryGameDao;
import dataaccess.MemoryUserDao;

public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server(
                new MemoryUserDao(),
                new MemoryAuthDao(),
                new MemoryGameDao()
        );

        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}