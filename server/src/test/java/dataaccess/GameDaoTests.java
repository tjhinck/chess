package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameDataDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameDaoTests {

    private static GameDao gameDao;

    @BeforeAll
    public static void init(){
        gameDao = new SqlGameDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDao.clearData();
    }

    @Test
    public void addGame() throws DataAccessException{
        gameDao.addGame(new GameData(1, "gamer", new ChessGame(), null, null));
        GameData game = gameDao.getGame(1);
        assertEquals(game.gameName(), "gamer");
    }

    @Test
    public void addAndClose() throws DataAccessException{
        gameDao.addGame(new GameData(1, "gamer", new ChessGame(), null, null));
        gameDao = null;
        gameDao = new SqlGameDao();
        assertNotNull(gameDao.getGame(1));
    }

    @Test
    public void updateGame() throws DataAccessException{
        gameDao.addGame(new GameData(1, "gamer", new ChessGame(), null, null));
        gameDao.updateGame(new GameData(1, "gamer", new ChessGame(), "white", "black"));
        GameData game = gameDao.getGame(1);
        assertEquals(game.whiteUsername(), "white");
        assertEquals(game.blackUsername(), "black");
    }

    @Test
    public void closeAndUpdate() throws DataAccessException{
        gameDao.addGame(new GameData(1, "gamer", new ChessGame(), null, null));
        gameDao = null;
        gameDao = new SqlGameDao();
        gameDao.updateGame(new GameData(1, "gamer", new ChessGame(), "white", "black"));
        GameData game = gameDao.getGame(1);
        assertEquals(game.whiteUsername(), "white");
        assertEquals(game.blackUsername(), "black");
    }

    @Test
    public void listGames() throws DataAccessException{
        GameData game = new GameData(1, "gamer", new ChessGame(), null, null);
        gameDao.addGame(game);
        GameDataDto gameDataDto = new GameDataDto(1, "gamer", null, null);
        assertTrue(gameDao.listGames().contains(gameDataDto));
    }

    @Test
    public void closeAndList() throws DataAccessException {
        GameData game = new GameData(1, "gamer", new ChessGame(), null, null);
        gameDao.addGame(game);
        gameDao = null;
        gameDao = new SqlGameDao();
        GameDataDto gameDataDto = new GameDataDto(1, "gamer", null, null);
        assertTrue(gameDao.listGames().contains(gameDataDto));
    }

    @Test
    public void invalidGet() throws DataAccessException{
        gameDao.addGame(new GameData(1, "gamer", new ChessGame(), null, null));
        assertNull(gameDao.getGame(23));
    }

    @Test
    public void closeAndGet() throws DataAccessException{
        gameDao.addGame(new GameData(1, "gamer", new ChessGame(), null, null));
        gameDao = null;
        gameDao = new SqlGameDao();
        assertEquals(gameDao.getGame(1).gameName(), "gamer");
    }

    @Test
    public void clear() throws DataAccessException{
        gameDao.addGame(new GameData(1, "gamer", new ChessGame(), null, null));
        gameDao.clearData();
        assertNull(gameDao.getGame(1));
    }
}
