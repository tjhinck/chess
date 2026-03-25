package client;

import java.util.Arrays;
import java.util.Scanner;

import exception.ResponseException;
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
        System.out.println("Welcome to chess. Please sign in to begin");
        System.out.print(help());

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
        System.out.println("Goodbye!");
        System.out.println();
    }

    private String eval(String input){
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signin" -> signIn(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String signIn(String... params){

    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR  + ">>> " + SET_TEXT_COLOR_GREEN);
    }


    private String help(){
        if (state == State.SIGNED_OUT){
            return """
                    - signIn <username>
                    - quit
                    """;
        }
        return """
                - list
                - quit
                """;
    }
}
