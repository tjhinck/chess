package service;

import response.ListGamesResponse;
import dataaccess.DataAccessException;
import dataaccess.GameDao;

public class ListGamesService {
    private final GameDao gameDao;

    public ListGamesService(GameDao gameDao){
        this.gameDao = gameDao;
    }

    public ListGamesResponse listGames() throws DataAccessException {
        return new ListGamesResponse(gameDao.listGames());
    }
}
