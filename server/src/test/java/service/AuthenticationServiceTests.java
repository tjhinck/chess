package service;

import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDao;
import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationServiceTests {

    private static AuthenticationService authenticationService;
    private static AuthDao authDao;

    @BeforeAll
    public static void init(){
        authDao = new MemoryAuthDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        authDao.clearData();
        authenticationService = new AuthenticationService(authDao);
    }

    @Test
    public void validAuth() throws DataAccessException {
        authDao.addAuthData(new AuthData("goodtoken", "username"));
        assertTrue(authenticationService.isValidToken("goodtoken"));

    }

    @Test
    public void invalidAuth() throws DataAccessException {
        authDao.addAuthData(new AuthData("goodtoken", "username"));
        assertFalse(authenticationService.isValidToken("badtoken"));
    }
}
