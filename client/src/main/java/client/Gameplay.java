package client;

import chess.*;
import chess.ChessGame.TeamColor;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.RegisterRequest;
import response.RegisterResponse;
import response.ResponseException;
import websocket.WsFacade;
import websocket.WsMessageHandler;
import websocket.messages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class Gameplay implements WsMessageHandler {
    private final WsFacade ws;
    private final String authToken;
    String gameName;
    Integer gameID;
    ChessGame chessGame;
    GameRole role;
    TeamColor color;


    public Gameplay(String serverURL, String authToken, Integer gameID, String gameName, GameRole role, TeamColor color) throws ResponseException {
        ws = new WsFacade(serverURL, this);
        this.authToken = authToken;
        this.gameID = gameID;
        this.gameName = gameName;
        this.role = role;
        this.color = color;
    }

    public void run() throws ResponseException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Exiting...");
            try {
                ws.disconnect(authToken, gameID);
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
        }));

        System.out.print("Starting ");
        System.out.println(gameName);
        ws.connect(authToken, gameID);

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals(goodbye)) {
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE);
                System.out.print(result);
                printPrompt();
            } catch (Throwable e) {
                System.out.print(SET_TEXT_COLOR_RED + "Error: Something went wrong. Please try again");
//                System.out.print(SET_TEXT_COLOR_RED + e.getMessage());
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
        System.out.println();
        System.out.print(SET_TEXT_COLOR_YELLOW);
        System.out.println(message);
    }

    private void displayError(String errorMessage){
        System.out.println();
        System.out.print(SET_TEXT_COLOR_RED);
        System.out.println(errorMessage);
        printPrompt();
    }

    private void loadGame(ChessGame chessGame){
        this.chessGame = chessGame;
        System.out.println();
        displayBoard(false, null, null);
        printPrompt();
    }

    private final String help = """
                help  -  view command options
                leave  -  leave current game
                redraw  -  redraw the board
                move <start> <end> <promotion> -  make move, ex: move e2 e4 or move a7 a8 queen
                show <piece>  -  display legal moves for a piece, ex: list b2
                resign  -  resign from the game
                """;

    private final String goodbye = "Thanks for playing";


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
                case "show" -> showMoves(params);
                case "resign" -> resign();
                default -> "Enter 'help' to view options";
            };
        } catch (ResponseException | IllegalArgumentException ex) {
            return SET_TEXT_COLOR_RED + ex.getMessage();
        } catch (IllegalStateException ex){
            return SET_TEXT_COLOR_RED + "Connection Error";
        } catch (ArrayIndexOutOfBoundsException ex){
            return SET_TEXT_COLOR_RED + "Invalid input. Enter 'help' to view required format";
        }
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
    }

    private ChessPosition positionParser(String positionStr){
        if (positionStr == null || positionStr.length() < 2) {
            throw new IllegalArgumentException("Invalid Square");
        }
        char file = positionStr.toLowerCase().charAt(0);
        char rank = positionStr.charAt(1);
        int col = (file - 'a') + 1;
        int row = (rank - '1') + 1;
        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType promotionPieceParser(String input){
        if (input == null || input.isEmpty()) {
            return null;
        }
        String promotionPieceStr = input.toUpperCase().trim();
        return switch (promotionPieceStr) {
            case "Q", "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "N", "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            case "R", "ROOK" -> ChessPiece.PieceType.ROOK;
            case "B", "BISHOP" -> ChessPiece.PieceType.BISHOP;
            default -> throw new IllegalArgumentException("Invalid promotion piece: " + input);
        };
    }

    private String move(String... params) throws ResponseException {
        if (chessGame.getTeamTurn() != color){
            throw new ResponseException(ResponseException.HttpCode.unauthorized,
                    String.format("It's %s's turn, not yours", chessGame.getTeamTurn().toString().toLowerCase()));
        }
        ChessPosition start = positionParser(params[0]);
        ChessPosition end = positionParser(params[1]);
        ChessPiece.PieceType promotionPiece;
        try {
            promotionPiece = promotionPieceParser(params[2]);
        } catch (ArrayIndexOutOfBoundsException ex){
           promotionPiece = null;
        }
        ChessMove move = new ChessMove(start, end, promotionPiece);
        Collection<ChessMove> validMoves = chessGame.validMoves(start);
        if (validMoves != null && validMoves.contains(move)){
            ws.makeMove(authToken, gameID, move);
        } else {
            throw new IllegalArgumentException("Illegal Move");
        }
        return "";
    }

    private String leave() throws ResponseException {
        ws.disconnect(authToken, gameID);
        return goodbye;
    }

    private String redraw(){
        displayBoard(false, null, null);
        return "";
    }

    private String resign() throws ResponseException {
        System.out.print(SET_TEXT_COLOR_RED);
        System.out.println("Are you sure you want to resign? (y/n)");
        printPrompt();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        input = input.toLowerCase();
        if (input.equals("y") || input.equals("yes")){
            ws.resign(authToken, gameID);
        }
        return "";
    }

    private String showMoves(String... params){
        ChessPosition target = positionParser(params[0]);
        Collection<ChessMove> validMoves = chessGame.validMoves(target);
        if (validMoves == null){
            return "Piece not found";
        }
        Collection<ChessPosition> highlightSquares = new ArrayList<>();
        for (ChessMove move : validMoves){
            highlightSquares.add(move.getEndPosition());
        }
        displayBoard(true, target, highlightSquares);
        return "";
    }

    private void displayBoard(Boolean highlight, ChessPosition target, Collection<ChessPosition> highlightSquares){
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
            if (highlight){
                for (int col = colStart; col != colEnd; col += colInc) {
                    out.print(ANSI_RESET);
                    ChessPiece piece = chessBoard.getPiece(row, col);
                    out.print(checkHighlightedSquareColor(row, col, target, highlightSquares));
                    out.print(pieceChar(piece));
                }
            } else {
                for (int col = colStart; col != colEnd; col += colInc) {
                    out.print(ANSI_RESET);
                    ChessPiece piece = chessBoard.getPiece(row, col);
                    out.print(squareColor(row, col));
                    out.print(pieceChar(piece));
                }
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

    private String checkHighlightedSquareColor(int row, int col, ChessPosition target, Collection<ChessPosition> highlightedSquares){
        ChessPosition positionToCheck = new ChessPosition(row, col);
        if (positionToCheck.equals(target)){
            return SET_BG_COLOR_MAGENTA;
        } else if (highlightedSquares.contains(positionToCheck)) {
            if ((row + col) % 2 == 0) {
                return SET_BG_COLOR_DARK_GREEN;
            }
            return SET_BG_COLOR_GREEN;
        } else {
            return squareColor(row, col);
        }
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

    public static void main(String[] args) throws ResponseException {
        String serverUrl = "http://localhost:8080";
        ServerFacade server = new ServerFacade(serverUrl);
        server.clear();
        RegisterRequest registerRequest = new RegisterRequest("user", "pass", "mail");
        RegisterResponse registerResponse = server.register(registerRequest);
        String authToken = registerResponse.authToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        int gameID = server.create(createGameRequest, authToken).gameID();
        JoinGameRequest joinGameRequest = new JoinGameRequest(gameID, TeamColor.WHITE);
        server.join(joinGameRequest, authToken);
        Gameplay gameplay = new Gameplay(serverUrl, authToken, gameID, "game", GameRole.PLAYER, TeamColor.WHITE);
        gameplay.run();
//        gameplay.move("d2", "d4");
    }
}
