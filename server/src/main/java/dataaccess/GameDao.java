package dataaccess;

import chess.ChessGame;

public interface GameDao {

    void addGame(int gameID, ChessGame chessGame) throws DataAccessException;



    void clearData() throws DataAccessException;
}
