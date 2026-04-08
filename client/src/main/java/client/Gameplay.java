package client;

import chess.*;
import model.GameData;
import chess.ChessGame.TeamColor;
import response.ResponseException;
import websocket.WsFacade;
import websocket.WsMessageHandler;
import websocket.messages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class Gameplay implements WsMessageHandler {
    private final WsFacade ws;
    private final String authToken;
    GameData gameData;
    ChessGame chessGame;
    GameRole role;
    TeamColor color;


    public Gameplay(String serverURL, String authToken, GameData gameData, GameRole role, TeamColor color) throws ResponseException {
        ws = new WsFacade(serverURL, this);
        this.authToken = authToken;
        this.gameData = gameData;
        this.role = role;
        this.color = color;
    }

    public void run() throws ResponseException {
        System.out.print("Starting ");
        System.out.println(gameData.gameName());
        ws.connect(authToken, gameData.gameID());

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

    @Override
    public void notify(ServerMessage message){
        switch (message.getServerMessageType()){
            case NOTIFICATION -> displayNotification(message.getMessage());
            case ERROR -> displayError(message.getErrorMessage());
            case LOAD_GAME -> loadGame(message.getGame());
        }
    }


    private void displayNotification(String message){
        System.out.print(SET_BG_COLOR_YELLOW);
        System.out.println(message);
    }

    private void displayError(String errorMessage){
        System.out.print(SET_TEXT_COLOR_RED);
        System.out.println(errorMessage);
    }

    private void loadGame(ChessGame chessGame){
        this.chessGame = chessGame;
        displayBoard();
    }

    private String help = """
                help  -  view command options
                leave  -  leave current game
                redraw  -  redraw the board
                move <start> <end> <promotion> -  make move ex: move e2 e4 or move a7 a8 queen
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
                case "leave" -> leave();
                case "redraw" -> redraw();
                case "move" -> move(params);
                default -> "Enter 'help' to view options";
            };
        } catch (ResponseException ex) {
            return SET_TEXT_COLOR_RED + ex.getMessage();
        } catch (IllegalArgumentException ex) {
            return SET_TEXT_COLOR_RED + ex.getMessage();
        } catch (ArrayIndexOutOfBoundsException ex){
            return SET_TEXT_COLOR_RED + "Invalid input. Enter 'help' to view required format";
        }
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
    }

    private ChessPosition positionParser(String positionStr){
        if (positionStr == null || positionStr.length() < 2) {
            throw new IllegalArgumentException("Invalid Move");
        }
        char file = positionStr.toLowerCase().charAt(0);
        char rank = positionStr.charAt(1);
        int col = file - 'a';
        int row = rank - '1';
        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType promotionPieceParser(String input){
        if (input == null || input.isEmpty()) {
            return null;
        }
        String promotionPieceStr = input.toUpperCase().trim();
        try {
            return ChessPiece.PieceType.valueOf(promotionPieceStr);
        } catch (IllegalArgumentException e) {
            return switch (promotionPieceStr) {
                case "Q" -> ChessPiece.PieceType.QUEEN;
                case "N" -> ChessPiece.PieceType.KNIGHT;
                case "R" -> ChessPiece.PieceType.ROOK;
                case "B" -> ChessPiece.PieceType.BISHOP;
                default -> throw new IllegalArgumentException("Invalid promotion piece: " + input);
            };
        }
    }

    private String move(String... params) throws ResponseException {
        ChessPosition start = positionParser(params[0]);
        ChessPosition end = positionParser(params[1]);
        ChessPiece.PieceType promotionPiece = promotionPieceParser(params[2]);
        ChessMove move = new ChessMove(start, end, promotionPiece);
        ws.makeMove(authToken, gameData.gameID(), move);
        return "";
    }

    private String leave() throws ResponseException {
        ws.disconnect(authToken, gameData.gameID());
        return goodbye;
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

//    public static void main(String[] args) throws ResponseException {
//        String serverUrl = "http://localhost:8080";
//        Gameplay gameplay = new Gameplay(serverUrl, new GameData(1, "game", new ChessGame(), null, null), Gameplay.Role.PLAYER, TeamColor.WHITE);
//        gameplay.run();
//    }
}
