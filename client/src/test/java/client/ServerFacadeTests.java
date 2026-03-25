package client;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import request.RegisterRequest;
import response.ResponseException;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static Client client;
    private static RegisterRequest registerRequest;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
//        client = new Client("http://localhost:" + port);
        System.out.println("Started server interface");
        registerRequest = new RegisterRequest("user", "password", "mail");
    }

    @BeforeEach
    public void setup() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


//    @Test
//    public void register() throws ResponseException {
//        assertDoesNotThrow(facade.register(registerRequest));
//    }

}
