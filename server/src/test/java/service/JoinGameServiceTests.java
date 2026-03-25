package service;

import chess.ChessGame;
import dataaccess.*;
import response.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.JoinGameRequest;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameServiceTests {

    private static JoinGameService joinGameService;
    private static GameDao gameDao;
    private static AuthDao authDao;


    @BeforeAll
    public static void init(){
        gameDao = new MemoryGameDao();
        authDao = new MemoryAuthDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDao.clearData();
        authDao.clearData();
        joinGameService = new JoinGameService(gameDao, authDao);
    }

    @Test
    public void joinSuccess() throws DataAccessException, ResponseException {
        GameData game = new GameData(1, "goodgame", new ChessGame(), null, null);
        AuthData auth = new AuthData("secrettoken", "username");
        gameDao.addGame(game);
        authDao.addAuthData(auth);
        JoinGameRequest request = new JoinGameRequest(1, ChessGame.TeamColor.WHITE);
        joinGameService.joinGame(request, auth.authToken());
        game = gameDao.getGame(1);
        assertEquals(game.whiteUsername(), "username");
    }

    @Test
    public void gameFull() throws DataAccessException, ResponseException {
        GameData game = new GameData(1, "goodgame", new ChessGame(), null, null);
        gameDao.addGame(game);
        AuthData whiteAuth = new AuthData("whitetoken", "white");
        AuthData blackAuth = new AuthData("blacktoken", "black");
        AuthData loser = new AuthData("loserToken", "letmeplay");
        authDao.addAuthData(whiteAuth);
        authDao.addAuthData(blackAuth);
        authDao.addAuthData(loser);
        joinGameService.joinGame(new JoinGameRequest(1, ChessGame.TeamColor.WHITE), whiteAuth.authToken());
        joinGameService.joinGame(new JoinGameRequest(1, ChessGame.TeamColor.BLACK), blackAuth.authToken());
        assertThrows(ResponseException.class, () -> joinGameService.joinGame(new JoinGameRequest(1, ChessGame.TeamColor.WHITE), loser.authToken()));
    }
}
