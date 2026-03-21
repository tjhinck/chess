package server;

import request.CreateGameRequest;
import request.JoinGameRequest;
import request.RegisterRequest;
import response.*;
import dataaccess.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.security.RouteRole;
import com.google.gson.Gson;
import request.LoginRequest;
import service.*;

import java.util.Map;

public class Server {

    public static final Gson GSON = new Gson();
    private final Javalin javalin;
    private final AuthenticationService authenticationService;
    private final LoginService loginService;
    private final RegisterService registerService;
    private final LogoutService logoutService;
    private final DeleteService deleteService;
    private final CreateGameService createGameService;
    private final JoinGameService joinGameService;
    private final ListGamesService listGamesService;

    enum Permission implements RouteRole {
        PUBLIC,
        AUTHENTICATED
    }

    public Server() {
        this(
            new SqlUserDao(),
            new SqlAuthDao(),
            new SqlGameDao()

//            new MemoryUserDao(),
//            new MemoryAuthDao(),
//            new MemoryGameDao()
        );
    }

    public Server(UserDao userDao, AuthDao authDao, GameDao gameDao) {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
            .post("/user", this::register, Permission.PUBLIC)
            .post("/session", this::login, Permission.PUBLIC)
            .delete("/session", this::logout, Permission.AUTHENTICATED)
            .delete("/db", this::delete)
            .get("/game", this::listGames, Permission.AUTHENTICATED)
            .post("/game", this::createGame, Permission.AUTHENTICATED)
            .put("/game", this::joinGame, Permission.AUTHENTICATED)
            .beforeMatched(this::checkPermission)
            .exception(ResponseException.class, this::responseExceptionHandler)
            .exception(Exception.class, this::serverErrorHandler);

        authenticationService = new AuthenticationService(authDao);
        registerService = new RegisterService(userDao, authDao);
        loginService = new LoginService(userDao, authDao);
        logoutService = new LogoutService(authDao);
        deleteService = new DeleteService(userDao, authDao, gameDao);
        createGameService = new CreateGameService(gameDao);
        joinGameService = new JoinGameService(gameDao, authDao);
        listGamesService = new ListGamesService(gameDao);
    }

    private void register(Context context) throws DataAccessException, ResponseException {
        RegisterRequest registerRequest = deserializeRequest(context.body(), RegisterRequest.class);
        RegisterResponse registerResponse = registerService.register(registerRequest);
        context.status(200);
        context.result(GSON.toJson(registerResponse));
    }

    private void login(Context context) throws DataAccessException, ResponseException {
        LoginRequest loginRequest = deserializeRequest(context.body(), LoginRequest.class);
        LoginResponse loginResponse = loginService.login(loginRequest);
        context.status(200);
        context.result(GSON.toJson(loginResponse));
    }

    private void logout(Context context) throws DataAccessException, ResponseException {
        String authToken = context.header("authorization");
        logoutService.logout(authToken);
        context.status(200);
    }

    private void delete(Context context) throws DataAccessException {
        deleteService.clearAll();
        context.status(200);
    }
    private void listGames(Context context) throws DataAccessException{
        ListGamesResponse listGamesResponse = listGamesService.listGames();
        context.status(200);
        context.result(GSON.toJson(listGamesResponse));
    }
    private void createGame(Context context) throws ResponseException, DataAccessException {
        CreateGameRequest createGameRequest = deserializeRequest(context.body(), CreateGameRequest.class);
        CreateGameResponse createGameResponse = createGameService.createGame(createGameRequest);
        context.status(200);
        context.result(GSON.toJson(createGameResponse));
    }
    private void joinGame(Context context) throws DataAccessException, ResponseException {
        JoinGameRequest joinGameRequest = deserializeRequest(context.body(), JoinGameRequest.class);
        String authToken = context.header("authorization");
        joinGameService.joinGame(joinGameRequest, authToken);
        context.status(200);
    }

    private void checkPermission(Context context) throws DataAccessException, ResponseException {
        var permission = context.routeRoles();
        if (permission.contains(Permission.AUTHENTICATED)){
            String authToken = context.header("authorization");
            if (!authenticationService.isValidToken(authToken)){
                throw new ResponseException(ResponseException.HttpCode.unauthorized, "Error: unauthorized");
            }
        }
    }

    private void responseExceptionHandler(ResponseException exception, Context context) {
        context.status(exception.code());
        context.result(exception.toJson());
    }

    private void serverErrorHandler(Exception exception, Context context){
        context.status(500);
        context.result(GSON.toJson(Map.of("message", exception.getMessage())));
    }

    private <T> T deserializeRequest(String json, Class<T> requestType) throws ResponseException{
        try{
            return GSON.fromJson(json, requestType);
        } catch (Exception e){
            throw new ResponseException(ResponseException.HttpCode.badRequest, "Error: bad request");
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
