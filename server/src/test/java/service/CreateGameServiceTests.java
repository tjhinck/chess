package service;

import dataaccess.DataAccessException;
import dataaccess.GameDao;
import dataaccess.MemoryGameDao;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateGameServiceTests {

    private static CreateGameService createGameService;
    private static GameDao gameDao;

    @BeforeAll
    public static void init(){
        gameDao = new MemoryGameDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDao.clearData();
        createGameService = new CreateGameService(gameDao);
    }

    @Test
    public void validNewGame() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest("coolgame");
        createGameService.createGame(request);
        GameData gamedata =  gameDao.getGame(1);
        assertEquals(gamedata.getGameName(), "coolgame");
    }

    @Test
    public void gameIDCheck() throws DataAccessException {
        createGameService.createGame(new CreateGameRequest("game1"));
        createGameService.createGame(new CreateGameRequest("game2"));
        createGameService.createGame(new CreateGameRequest("game3"));
        assertNotNull(gameDao.getGame(1));
        assertNotNull(gameDao.getGame(2));
        assertNotNull(gameDao.getGame(3));
    }
}
