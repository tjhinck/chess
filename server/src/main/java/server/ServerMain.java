package server;

import dataaccess.*;

public class ServerMain {
    public static void main(String[] args) throws DataAccessException {
        Server server = new Server(
                new SqlUserDao(),
                new SqlAuthDao(),
                new SqlGameDao()
        );

        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}