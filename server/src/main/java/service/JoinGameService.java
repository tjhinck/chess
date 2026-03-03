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
            throw new ResponseException(ResponseException.httpCode.badRequest, "Error: bad request");
        }
    }

    private void addPlayer(TeamColor playerColor, String username, GameData gameData) throws ResponseException {
        if (playerColor == TeamColor.WHITE){
            if (gameData.isWhiteOpen()){
                gameData.setWhiteUsername(username);
            } else {
                throw new ResponseException(ResponseException.httpCode.alreadyTaken, "Error: already taken");
            }
        } else if (playerColor == TeamColor.BLACK) {
            if (gameData.isBlackOpen()){
                gameData.setBlackUsername(username);
            } else {
                throw new ResponseException(ResponseException.httpCode.alreadyTaken, "Error: already taken");
            }
        } else {
            throw new ResponseException(ResponseException.httpCode.badRequest, "Error: bad request");
        }
    }
}
