package client;

import java.util.Arrays;
import java.util.Scanner;

import chess.ChessGame;
import chess.GameRole;
import model.GameData;
import request.*;
import response.*;
import ui.EnumeratedGameList;

import static ui.EscapeSequences.*;

public class Client{
    private final String serverURL;
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private String authToken;
    private EnumeratedGameList games;

    private enum State {
        LOGGED_IN("[Logged In]"),
        LOGGED_OUT("[Logged Out]");

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
        this.serverURL = serverURL;
        server = new ServerFacade(this.serverURL);
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
                System.out.print(SET_TEXT_COLOR_RED + "Error: Something went wrong. Please try again");
//                System.out.print(SET_TEXT_COLOR_RED + e.getMessage());
            }
        }
        System.out.println();
    }

    private String eval(String input){
        try {
            if (input.isBlank()){
                return "";
            }
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observer(params);
                case "help" -> help();
                case "quit" -> "Goodbye!";
                default -> "Enter 'help' to view options";
            };
        } catch (ResponseException ex) {
            return SET_TEXT_COLOR_RED + ex.getMessage();
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex){
            return SET_TEXT_COLOR_RED + "Invalid input. Enter 'help' to view required format";
        }
    }

    private String login(String... params) throws ResponseException{
        LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
        LoginResponse loginResponse = server.login(loginRequest);
        state = State.LOGGED_IN;
        authToken = loginResponse.authToken();
        return "Hello, " + loginResponse.username();
    }

    private String register(String... params) throws ResponseException{
        RegisterRequest registerRequest = new RegisterRequest(params[0], params[1], params[2]);
        RegisterResponse registerResponse = server.register(registerRequest);
        state = State.LOGGED_IN;
        authToken = registerResponse.authToken();
        return "Welcome, " + registerResponse.username();
    }

    private String logout() throws ResponseException{
        assertSignedIn();
        server.logout(authToken);
        state = State.LOGGED_OUT;
        authToken = null;
        return "Until next time...";
    }

    private String create(String... params) throws ResponseException {
        assertSignedIn();
        CreateGameRequest createGameRequest = new CreateGameRequest(params[0]);
        CreateGameResponse createGameResponse = server.create(createGameRequest, authToken);
        ListGamesResponse listGamesResponse = server.list(authToken);
        games = new EnumeratedGameList(listGamesResponse.games());
        return "Created new game";
    }

    private String list() throws ResponseException{
        assertSignedIn();
        ListGamesResponse listGamesResponse = server.list(authToken);
        games = new EnumeratedGameList(listGamesResponse.games());
        return games.toString();
    }

    private String join(String... params) throws ResponseException {
        assertSignedIn();
        int gameNum = Integer.parseInt(params[0]);
        ChessGame.TeamColor color = ChessGame.TeamColor.fromValue(params[1]);
        if (games == null){
            ListGamesResponse listGamesResponse = server.list(authToken);
            games = new EnumeratedGameList(listGamesResponse.games());
        }

        GameData selectedGame;
        try{
            selectedGame = games.get(gameNum);
        } catch (IndexOutOfBoundsException ex) {
            return "Game " + gameNum + " not found";
        }
        JoinGameRequest joinGameRequest = new JoinGameRequest(selectedGame.gameID(), color);
        server.join(joinGameRequest, authToken);
        Gameplay gameplay = new Gameplay(serverURL, authToken, selectedGame.gameID(), selectedGame.gameName(), GameRole.PLAYER, color);
        gameplay.run();
        return "";
    }

    private String observer(String... params) throws ResponseException {
        assertSignedIn();
        int gameNum = Integer.parseInt(params[0]);
        if (games == null){
            ListGamesResponse listGamesResponse = server.list(authToken);
            games = new EnumeratedGameList(listGamesResponse.games());
        }

        GameData selectedGame;
        try{
            selectedGame = games.get(gameNum);
        } catch (IndexOutOfBoundsException ex) {
            return "Game " + gameNum + " not found";
        }
        Gameplay gameplay = new Gameplay(serverURL, authToken, selectedGame.gameID(), selectedGame.gameName(), GameRole.OBSERVER, ChessGame.TeamColor.WHITE);
        gameplay.run();
        return "";
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + state + " >>> " + SET_TEXT_COLOR_GREEN);
    }


    private String help(){
        if (state == State.LOGGED_OUT){
            return """
                    help  -  view command options
                    quit  -  exit game
                    login <username> <password>  -  login with existing account
                    register <username> <password> <email>  -  create a new account
                    """;
        }
        return """
                help  -  view command options
                quit  -  exit the chess application
                logout  -  logout current user
                list  -  list current chess games
                observe <game number>  -  spectate a chess game
                create <game name>  -  create new chess game
                join <game number> [white|black] -  join a chess game
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGED_OUT) {
            throw new ResponseException(ResponseException.HttpCode.badRequest, "Please sign in first");
        }
    }
}
