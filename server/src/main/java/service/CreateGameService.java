package service;

import Request.CreateGameRequest;
import Response.CreateGameResponse;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import model.GameData;

public class CreateGameService {
    private final GameDao gameDao;
    private int newGameID = 0;

    public CreateGameService(GameDao gameDao){
        this.gameDao = gameDao;
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        newGameID++;
        GameData newGame = new GameData(newGameID, request.gameName(), new ChessGame());
        gameDao.addGame(newGame);
        return new CreateGameResponse(newGameID);
    }
}
