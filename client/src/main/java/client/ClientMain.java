package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
//        var board = new ChessBoard();
//        var copy = board.clone();
//        board.resetBoard();
//        System.out.println(board);
//        System.out.println(copy);

        String serverUrl;
        if (args.length > 1) {
            serverUrl = args[0];
        } else {
            serverUrl = "http://localhost:8080";
        }
        try {
            new Client(serverUrl).run();

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}
