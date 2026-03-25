package client;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import request.RegisterRequest;
import response.RegisterResponse;
import response.ResponseException;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setup() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void register() {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "mail");
        assertDoesNotThrow(() -> facade.register(registerRequest));
    }

    @Test
    public void registerUsernameTaken() throws ResponseException {
        RegisterRequest goodRequest = new RegisterRequest("user", "password", "mail");
        RegisterRequest badrequest = new RegisterRequest("user", "bigword", "badmail");
        facade.register(goodRequest);
        assertThrows(ResponseException.class, () -> facade.register(badrequest));
    }

}
