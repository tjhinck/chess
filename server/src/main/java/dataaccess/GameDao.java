package dataaccess;

import model.GameData;

public interface GameDao {

    void addGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void clearData() throws DataAccessException;
}
