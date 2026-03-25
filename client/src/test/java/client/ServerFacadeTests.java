package client;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.RegisterResponse;
import response.ResponseException;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setup() throws ResponseException {
        facade = new ServerFacade("http://localhost:" + port);
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

    @Test
    public void login() throws ResponseException {
        RegisterRequest goodRequest = new RegisterRequest("user", "password", "mail");
        facade.register(goodRequest);
        facade = new ServerFacade("http://localhost:" + port);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        assertDoesNotThrow(() -> facade.login(loginRequest));
    }

    @Test
    public void loginUnregistered() {
        LoginRequest badLogin = new LoginRequest("user", "password");
        assertThrows(ResponseException.class, () -> facade.login(badLogin));
    }

    @Test
    public void logout() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "mail");
        RegisterResponse registerResponse = facade.register(registerRequest);
        assertDoesNotThrow(() -> facade.logout(registerResponse.authToken()));
    }

    @Test
    public void logoutBadAuth() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "mail");
        facade.register(registerRequest);
        assertThrows(ResponseException.class, () -> facade.logout("badauth"));
    }

    @Test
    public void createGame() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "mail");
        RegisterResponse registerResponse = facade.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("coolgame");
        assertDoesNotThrow(() -> facade.create(createGameRequest, registerResponse.authToken()));
    }

    @Test
    public void createBadAuth(){
        CreateGameRequest createGameRequest = new CreateGameRequest("coolgame");
        assertThrows(ResponseException.class, () -> facade.create(createGameRequest, "badauth"));
    }

    @Test
    public void list() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "mail");
        RegisterResponse registerResponse = facade.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("coolgame");
        facade.create(createGameRequest, registerResponse.authToken());
        assertDoesNotThrow(() -> facade.list(registerResponse.authToken()));
    }

    @Test
    public void listBadAuth(){
        assertThrows(ResponseException.class, () -> facade.list("badauth"));
    }

}
