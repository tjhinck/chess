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
            if (isWhiteOpen(gameData)){
                GameData updatedGame = setWhiteUsername(gameData, username);
                gameDao.updateGame(updatedGame);
            } else {
                throw new ResponseException(ResponseException.HttpCode.alreadyTaken, "Error: already taken");
            }
        } else if (playerColor == TeamColor.BLACK) {
            if (isBlackOpen(gameData)){
                GameData updatedGame = setBlackUsername(gameData, username);
                gameDao.updateGame(updatedGame);
            } else {
                throw new ResponseException(ResponseException.HttpCode.alreadyTaken, "Error: already taken");
            }
        } else {
            throw new ResponseException(ResponseException.HttpCode.badRequest, "Error: bad request");
        }
    }

    private boolean isWhiteOpen(GameData gameData){
        return gameData.whiteUsername() == null;
    }

    private boolean isBlackOpen(GameData gameData){
        return gameData.blackUsername() == null;
    }

    private GameData setWhiteUsername(GameData gameData, String whiteUsername){
        return new GameData(gameData.gameID(), gameData.gameName(), gameData.chessGame(), whiteUsername, gameData.blackUsername());
    }

    private GameData setBlackUsername(GameData gameData, String blackUsername){
        return new GameData(gameData.gameID(), gameData.gameName(), gameData.chessGame(), gameData.whiteUsername(), blackUsername);
    }
}
