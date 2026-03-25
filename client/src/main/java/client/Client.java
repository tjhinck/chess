package client;

import java.util.Arrays;
import java.util.Scanner;

import response.ResponseException;
import server.ServerFacade;

import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade server;
    private State state = State.SIGNED_OUT;

    private enum State {
        SIGNED_IN,
        SIGNED_OUT
    }

    public Client(String serverURL){
        server = new ServerFacade(serverURL);
    }

    public void run(){
        System.out.println("♕ Welcome to chess" );
        System.out.println("Please sign in or register to begin");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
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
                case "quit" -> "Goodbye!";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String login(String... params) throws ResponseException{
        return "Immense failure";
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR  + ">>> " + SET_TEXT_COLOR_GREEN);
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
                create <name>  -  create new chess game
                list  -  list current chess games
                join <ID>  -  join a chess game
                observe <ID>  -  spectate a chess game
                logout  -  logout current user
                """;
    }
}
