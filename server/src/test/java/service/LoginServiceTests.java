package service;

import exception.ResponseException;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import response.LoginResponse;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class LoginServiceTests {

    private static LoginService loginService;
    private static UserDao userDao;
    private static AuthDao authDao;
    private static UserData testUser;
    private LoginRequest loginRequest;

    @BeforeAll
    public static void init(){
        userDao = new MemoryUserDao();
        authDao = new MemoryAuthDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDao.clearData();
        authDao.clearData();
        loginService = new LoginService(userDao, authDao);
        String passwordHash = BCrypt.hashpw("secretpassword", BCrypt.gensalt());
        testUser = new UserData("username", passwordHash, "hey@yahoo.com");
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
