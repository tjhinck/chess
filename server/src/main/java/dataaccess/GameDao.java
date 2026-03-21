package dataaccess;

import model.GameData;
import model.GameDataDto;

import java.util.Collection;

public interface GameDao {

    void addGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameDataDto> listGames() throws DataAccessException;

    void updateGame(GameData updatedGame) throws DataAccessException;

    void clearData() throws DataAccessException;
}
