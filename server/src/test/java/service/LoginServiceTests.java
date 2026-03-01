package service;

import Request.LoginRequest;
import Response.LoginResponse;
import Response.ResponseException;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDao;
import dataaccess.UserDao;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class LoginServiceTests {

    private static LoginService loginService;
    private static UserDao userDao;
    private static UserData testUser;
    private LoginRequest loginRequest;

    @BeforeAll
    public static void init(){
        userDao = new MemoryUserDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDao.clearData();
        loginService = new LoginService(userDao);
        testUser = new UserData("username", "secretpassword", "hey@yahoo.com");
        userDao.addUser(testUser);
    }

    @Test
    public void validLogin() throws ResponseException, DataAccessException {
        loginRequest = new LoginRequest("username", "secretpassword");
        LoginResponse response = loginService.login(loginRequest);
        assertEquals(response.username(), testUser.username());
    }

    @Test
    public void invalidLogin() throws DataAccessException, ResponseException {
        loginRequest = new LoginRequest("notmyname", "lesssecretpassword");
        assertThrows(ResponseException.class, () -> loginService.login(loginRequest));
    }
}
