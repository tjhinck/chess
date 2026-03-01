package server;

import Request.RegisterRequest;
import Response.RegisterResponse;
import Response.ResponseException;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDao;
import dataaccess.UserDao;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.google.gson.Gson;
import Request.LoginRequest;
import Response.LoginResponse;
import service.LoginService;
import service.RegisterService;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final LoginService loginService;
    private final RegisterService registerService;
    public static final Gson gson = new Gson();

    public Server(){
        this(new MemoryUserDao()
        );
    }

    public Server(UserDao userDao) {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
        // Register your endpoints and exception handlers here.
            .post("/user", this::register)
            .post("/session", this::login)
            .delete("/session", this::logout)
            .delete("/db", this::delete)
            .get("/game", this::listGames)
            .post("/game", this::createGame)
            .put("/game", this::joinGame)
            .exception(ResponseException.class, this::responseExceptionHandler)
            .exception(Exception.class, this::serverErrorHandler);

        loginService = new LoginService(userDao);
        registerService = new RegisterService(userDao);
    }

    private void register(Context context) throws DataAccessException, ResponseException {
        RegisterRequest registerRequest = (RegisterRequest) deserializeRequest(context, RegisterRequest.class);
        RegisterResponse registerResponse = registerService.register(registerRequest);
        context.status(200);
        context.result(gson.toJson(registerResponse));
    }

    private void login(Context context) throws ResponseException, DataAccessException {
//        LoginRequest loginRequest = gson.fromJson(context.body(), LoginRequest.class);
        LoginRequest loginRequest = (LoginRequest) deserializeRequest(context, LoginRequest.class);
        LoginResponse loginResponse = loginService.login(loginRequest);
        context.status(200);
        context.result(gson.toJson(loginResponse));
    }

    private void logout(Context context) {

    }
    private void delete(Context context) {

    }
    private void listGames(Context context) {

    }
    private void createGame(Context context) {

    }
    private void joinGame(Context context) {

    }

    private void responseExceptionHandler(ResponseException exception, Context context) {
        context.status(exception.code());
        context.result(exception.toJson());
    }

    private void serverErrorHandler(Exception exception, Context context){
        context.status(500);
        context.result(gson.toJson(Map.of(
                "message", exception.getMessage()
        )));
    }

    private <T> Object deserializeRequest(Context context, T requestType) throws ResponseException{
        try{
            return gson.fromJson(context.body(), requestType.getClass());
        } catch (Exception e){
            throw new ResponseException(ResponseException.httpCode.badRequest, "Error: bad request");
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
