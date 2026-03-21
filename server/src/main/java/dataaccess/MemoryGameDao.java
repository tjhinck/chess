package dataaccess;

import model.GameData;
import model.GameDataDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDao implements GameDao{
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void addGame(GameData gameData) throws DataAccessException {
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public Collection<GameDataDto> listGames() throws DataAccessException {
        Collection<GameDataDto> gameList = new ArrayList<>();
        for (GameData game : games.values()) {
            gameList.add(new GameDataDto(game.gameID(), game.gameName(), game.whiteUsername(), game.blackUsername()));
        }
        return gameList;
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        games.replace(updatedGame.gameID(), updatedGame);
    }

    @Override
    public void clearData() throws DataAccessException {
        games.clear();
    }
}
