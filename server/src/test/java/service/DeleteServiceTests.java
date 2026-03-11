package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteServiceTests {

    private static DeleteService deleteService;
    private static UserDao userDao;
    private static AuthDao authDao;
    private static GameDao gameDao;

    @BeforeAll
    public static void init(){
        userDao = new MemoryUserDao();
        authDao = new MemoryAuthDao();
        gameDao = new MemoryGameDao();
    }

    @BeforeEach
    public void setup(){
        deleteService = new DeleteService(userDao, authDao, gameDao);
    }

    @Test
    public void userCleared() throws DataAccessException {
        userDao.addUser(new UserData("username", "passwordHash", "mailbox"));
        deleteService.clearAll();
        assertNull(userDao.getUser("username"));
    }

    @Test
    public void authCleared() throws DataAccessException {
        authDao.addAuthData(new AuthData("token", "username"));
        deleteService.clearAll();
        assertNull(authDao.getAuthData("username"));
    }

    @Test
    public void gameCleared() throws DataAccessException {
        gameDao.addGame(new GameData(1, "coolgame", new ChessGame(), null, null));
        deleteService.clearAll();
        assertNull(gameDao.getGame(1));
    }
}
