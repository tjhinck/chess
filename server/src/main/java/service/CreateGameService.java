package service;

import Request.CreateGameRequest;
import Response.CreateGameResponse;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDao;

public class CreateGameService {
    private final GameDao gameDao;
    private int newGameID = 0;

    public CreateGameService(GameDao gameDao){
        this.gameDao = gameDao;
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        ChessGame newGame = new ChessGame();
        newGameID++;
        gameDao.addGame(newGameID, newGame);
        return new CreateGameResponse(newGameID);
    }
}
