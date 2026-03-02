package dataaccess;

import chess.ChessGame;

import java.util.HashMap;

public class MemoryGameDao implements GameDao{
    final private HashMap<Integer, ChessGame> games = new HashMap<>();

    @Override
    public void addGame(int gameID, ChessGame chessGame) throws DataAccessException {
        games.put(gameID, chessGame);
    }

    @Override
    public void clearData() throws DataAccessException {
        games.clear();
    }
}
