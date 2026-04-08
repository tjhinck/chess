package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;
import chess.ChessGame.TeamColor;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class Gameplay {
    GameData gameData;
    ChessGame chessGame;
    Role role;
    TeamColor color;

    public enum Role{
        PLAYER,
        OBSERVER
    }

    public Gameplay(GameData gameData, Role role, TeamColor color) {
        this.gameData = gameData;
        this.role = role;
        this.color = color;
    }

    public void run(){
        chessGame = gameData.chessGame();
        System.out.print("Starting ");
        System.out.println(gameData.gameName());
        displayBoard();

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals(goodbye)) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
//                displayBoard();
                System.out.print(SET_TEXT_COLOR_BLUE);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(SET_TEXT_COLOR_RED);
                System.out.print("Error: ");
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private String help = """
                help  -  view command options
                leave  -  leave current game
                redraw  -  redraw the board
                """;

    private String goodbye = "Thanks for playing";


    private String eval(String input){
        try {
            if (input.isBlank()){
                return "";
            }
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help;
                case "leave" -> goodbye;
                case "redraw" -> redraw();
                default -> "Enter 'help' to view options";
            };
        } catch (Exception ex) {
            return SET_TEXT_COLOR_RED + ex.getMessage();
        }
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
    }

    private String redraw(){
        displayBoard();
        return "";
    }

    private void displayBoard(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ChessBoard chessBoard = chessGame.getBoard();
        String columns;
        int rowStart;
        int rowEnd;
        int rowInc;
        int colStart;
        int colEnd;
        int colInc;
        if (color == TeamColor.BLACK){
            columns = "    H   G   F  E   D   C  B  A     ";
            rowStart = 1;
            rowEnd = 9;
            rowInc = 1;
            colStart = 8;
            colEnd = 0;
            colInc = -1;
        } else {
            columns = "    A   B   C  D   E   F  G  H     ";
            rowStart = 8;
            rowEnd = 0;
            rowInc = -1;
            colStart = 1;
            colEnd = 9;
            colInc = 1;
        }

        out.print(ERASE_SCREEN);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(columns);
        out.println(ANSI_RESET);

        for (int row = rowStart; row != rowEnd; row += rowInc){
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(" " + row + " ");
            out.print(ANSI_RESET);
            for (int col = colStart; col != colEnd; col += colInc){
                out.print(ANSI_RESET);
                ChessPiece piece = chessBoard.getPiece(row, col);
                out.print(squareColor(row, col));
                out.print(pieceChar(piece));
            }
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(" " + row + " ");
            out.println(RESET_BG_COLOR);
        }
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(columns);
        out.println(RESET_BG_COLOR);
    }

    private String squareColor(int row, int col){
        if ((row + col) % 2 == 0){
            return ANSI_BLACK_SQUARE;
        }
        return ANSI_WHITE_SQUARE;
    }

    private String pieceChar(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        } else {
            String color = switch (piece.getTeamColor()) {
                case WHITE -> SET_TEXT_COLOR_WHITE;
                case BLACK -> SET_TEXT_COLOR_BLACK;
            };
            String pieceChar = switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
            return color + pieceChar;
        }
    }

    public static void main(String[] args) {
        Gameplay gameplay = new Gameplay(new GameData(1, "game", new ChessGame(), null, null), Gameplay.Role.PLAYER, TeamColor.WHITE);
        gameplay.run();
    }
}
