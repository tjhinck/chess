package dataaccess;

import model.GameData;
import model.GameData;

import java.util.Collection;
import java.util.List;

public interface GameDao {

    void addGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    List<GameData> listGames() throws DataAccessException;

    void updateGame(GameData updatedGame) throws DataAccessException;

    void clearData() throws DataAccessException;
}
