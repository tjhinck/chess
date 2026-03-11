package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import dataaccess.MemoryGameDao;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import response.ListGamesResponse;

public class ListGamesServiceTests {
    private static ListGamesService listGamesService;
    private static GameDao gameDao;

    @BeforeAll
    public static void init(){
        gameDao = new MemoryGameDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDao.clearData();
        listGamesService = new ListGamesService(gameDao);
    }

    @Test
    public void showGame() throws DataAccessException {
        GameData game = new GameData(1, "goodgame", new ChessGame(), null, null);
        gameDao.addGame(game);
        ListGamesResponse listGamesResponse = listGamesService.listGames();
        assertTrue(listGamesResponse.games().contains(game));
    }

    @Test
    public void noGames() throws DataAccessException {
        ListGamesResponse listGamesResponse = listGamesService.listGames();
        assertTrue(listGamesResponse.games().isEmpty());
    }
}
