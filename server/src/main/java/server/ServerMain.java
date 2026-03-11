package server;

import dataaccess.*;
import response.ResponseException;

public class ServerMain {
    public static void main(String[] args) throws DataAccessException {
        Server server = new Server(
                new MemoryUserDao(),
                new SqlAuthDao(),
                new MemoryGameDao()
        );

        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}