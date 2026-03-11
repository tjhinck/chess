package dataaccess;

import chess.ChessGame;
import model.GameData;
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
    public void updateGame() throws DataAccessException{
        gameDao.addGame(new GameData(1, "gamer", new ChessGame(), null, null));
        gameDao.updateGame(new GameData(1, "gamer", new ChessGame(), "white", "black"));
        GameData game = gameDao.getGame(1);
        assertEquals(game.whiteUsername(), "white");
        assertEquals(game.blackUsername(), "black");
    }
}
