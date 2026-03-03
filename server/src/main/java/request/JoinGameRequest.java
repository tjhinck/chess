package request;

import chess.ChessGame;

import java.util.Objects;

public record JoinGameRequest(
        int gameID,
        ChessGame.TeamColor playerColor
        ) {
    public JoinGameRequest{
        Objects.requireNonNull(gameID, "gameID required");
        Objects.requireNonNull(playerColor, "playerColor required");
    }
}
