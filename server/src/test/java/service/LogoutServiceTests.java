package service;

import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDao;
import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LogoutServiceTests {

    private static LogoutService logoutService;
    private static AuthDao authDao;
    private static AuthData testAuth;

    @BeforeAll
    public static void init(){
        authDao = new MemoryAuthDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        authDao.clearData();
        logoutService = new LogoutService(authDao);

    }

    @Test
    public void validLogout() throws DataAccessException, ResponseException {
        String authToken = "authentication";
        testAuth = new AuthData(authToken, "username");
        authDao.addAuthData(testAuth);
        logoutService.logout(authToken);
        assertNull(authDao.getAuthData(authToken));
    }

    @Test
    public void invalidLogout() throws DataAccessException {
        String authToken = "authentication";
        String invalidAuthToken = "what-is-this";
        testAuth = new AuthData(authToken, "username");
        authDao.addAuthData(testAuth);
        assertThrows(ResponseException.class, () -> logoutService.logout(invalidAuthToken));
    }
}
