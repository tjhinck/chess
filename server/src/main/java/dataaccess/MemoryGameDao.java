package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> gameList = new ArrayList<>();
        for (GameData game : games.values()) {
            gameList.add(new GameData(game.gameID(), game.gameName(), game.chessGame(), game.whiteUsername(), game.blackUsername()));
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
