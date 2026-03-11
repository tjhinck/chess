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
}
