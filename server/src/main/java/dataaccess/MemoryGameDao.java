package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDao implements GameDao{
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void addGame(GameData gameData) throws DataAccessException {
        games.put(gameData.getGameID(), gameData);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public void clearData() throws DataAccessException {
        games.clear();
    }
}
