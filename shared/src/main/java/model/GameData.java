package model;

import chess.ChessGame;

import java.util.Objects;

public record GameData(
        int gameId,
        String gameName,
        ChessGame chessGame,
        String whiteUsername,
        String blackUsername) {
    public GameData {
        Objects.requireNonNull(gameId, "gameID required");
        Objects.requireNonNull(gameName, "gameName required");
        Objects.requireNonNull(chessGame, "chessGame required");
    }
}
