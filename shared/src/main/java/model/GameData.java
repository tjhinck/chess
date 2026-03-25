package model;

import chess.ChessGame;

import java.util.Objects;

public record GameData(
        int gameID,
        String gameName,
        ChessGame chessGame,
        String whiteUsername,
        String blackUsername) {
    public GameData {
        Objects.requireNonNull(gameID, "gameID required");
        Objects.requireNonNull(gameName, "gameName required");
        Objects.requireNonNull(chessGame, "chessGame required");
    }

    private boolean isEmpty(String str){
        return (str == null || str.isBlank());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(gameName);
        sb.append(" -- White: ");
        sb.append(isEmpty(whiteUsername) ? "open" : whiteUsername );
        sb.append( ", Black: ");
        sb.append(isEmpty(blackUsername) ? "open" : blackUsername );
        return sb.toString();
    }
}
