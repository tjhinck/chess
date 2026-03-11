package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SqlGameDao implements GameDao{
    @Override
    public void addGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void clearData() throws DataAccessException {

    }
}
