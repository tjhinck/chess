package service;

import request.JoinGameRequest;
import response.ResponseException;
import chess.ChessGame.TeamColor;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import model.AuthData;
import model.GameData;

public class JoinGameService {
    private final GameDao gameDao;
    private final AuthDao authDao;

    public JoinGameService(GameDao gameDao, AuthDao authDao){
        this.gameDao = gameDao;
        this.authDao = authDao;
    }

    public void joinGame(JoinGameRequest request, String authToken) throws DataAccessException, ResponseException {
        GameData foundGame = gameDao.getGame(request.gameID());
        if (foundGame != null){
            AuthData foundAuth = authDao.getAuthData(authToken);
            addPlayer(request.playerColor(), foundAuth.username(), foundGame);
        } else {
            throw new ResponseException(ResponseException.HttpCode.badRequest, "Error: bad request");
        }
    }

    private void addPlayer(TeamColor playerColor, String username, GameData gameData) throws ResponseException, DataAccessException {
        if (playerColor == TeamColor.WHITE){
            if (gameData.whiteUsername() == null){
                GameData updatedGame = new GameData(gameData.gameID(), gameData.gameName(), gameData.chessGame(), username, gameData.blackUsername());
                gameDao.updateGame(updatedGame);
            } else {
                throw new ResponseException(ResponseException.HttpCode.alreadyTaken, "Error: already taken");
            }
        } else if (playerColor == TeamColor.BLACK) {
            if (gameData.blackUsername() == null){
                GameData updatedGame = new GameData(gameData.gameID(), gameData.gameName(), gameData.chessGame(), gameData.whiteUsername(), username);
                gameDao.updateGame(updatedGame);
            } else {
                throw new ResponseException(ResponseException.HttpCode.alreadyTaken, "Error: already taken");
            }
        } else {
            throw new ResponseException(ResponseException.HttpCode.badRequest, "Error: bad request");
        }
    }
}
