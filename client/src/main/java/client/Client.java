package client;

import java.util.Arrays;
import java.util.Scanner;

import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;
import server.ServerFacade;

import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade server;
    private State state = State.SIGNED_OUT;
    private String authToken;

    private enum State {
        SIGNED_IN("[Signed In]"),
        SIGNED_OUT("[Signed Out]");

        private final String value;

        State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public Client(String serverURL){
        server = new ServerFacade(serverURL);
    }

    public void run(){
        System.out.println("♕ Welcome to chess");
        System.out.println("Please sign in or register to begin");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("Goodbye!")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(SET_TEXT_COLOR_RED + "Error: " + msg);
            }
        }
        System.out.println();
    }

    private String eval(String input){
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "help" -> help();
                case "quit" -> "Goodbye!";
                default -> "Unknown command";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String login(String... params) throws ResponseException{
        LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
        LoginResponse loginResponse = server.login(loginRequest);
        state = State.SIGNED_IN;
        authToken = loginResponse.authToken();
        return "Hello, " + loginResponse.username();
    }

    private String register(String... params) throws ResponseException{
        RegisterRequest registerRequest = new RegisterRequest(params[0], params[1], params[2]);
        RegisterResponse registerResponse = server.register(registerRequest);
        state = State.SIGNED_IN;
        authToken = registerResponse.authToken();
        return "Welcome, " + registerResponse.username();
    }

    private String logout() throws ResponseException{
        assertSignedIn();
        server.logout(authToken);
        state = State.SIGNED_OUT;
        authToken = null;
        return "Until next time...";
    }

    private String create(String... params) throws ResponseException {
        assertSignedIn();
        CreateGameRequest createGameRequest = new CreateGameRequest(params[0]);
        CreateGameResponse createGameResponse = server.create(createGameRequest, authToken);
        return "Created game " + createGameResponse.gameID();
    }

    private String list() throws ResponseException{
        assertSignedIn();
        ListGamesResponse listGamesResponse = server.list(authToken);
        var games = EnumeratedGameList.of(listGamesResponse.games());
        return games.toString();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + state + ">>> " + SET_TEXT_COLOR_GREEN);
    }


    private String help(){
        if (state == State.SIGNED_OUT){
            return """
                    help  -  view command options
                    quit  -  exit the chess application
                    login <username> <password>  -  login with existing account
                    register <username> <password> <email>  -  create a new account
                    """;
        }
        return """
                help  -  view command options
                quit  -  exit the chess application
                logout  -  logout current user
                list  -  list current chess games
                create <name>  -  create new chess game
                join <ID>  -  join a chess game
                observe <ID>  -  spectate a chess game
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNED_OUT) {
            throw new ResponseException(ResponseException.HttpCode.badRequest, "Please sign in first");
        }
    }
}
