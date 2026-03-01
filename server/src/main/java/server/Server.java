package server;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
        // Register your endpoints and exception handlers here.
            .post("/user", this::register)
            .post("/session", this::login)
            .delete("/session", this::logout)
            .delete("/db", this::delete)
            .get("game", this::listGames)
            .post("/game", this::createGame)
            .put("/game", this::joinGame);
    }

    private void register(Context context) {

    }

    private void login(Context context) {

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

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
