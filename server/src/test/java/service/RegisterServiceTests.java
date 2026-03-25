package service;

import response.ResponseException;
import request.RegisterRequest;
import response.RegisterResponse;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegisterServiceTests {

    private RegisterService registerService;
    private static UserDao userDao;
    private static AuthDao authDao;
    private UserData existingUser;
    private RegisterRequest registerRequest;

    @BeforeAll
    public static void init(){
        userDao = new MemoryUserDao();
        authDao = new MemoryAuthDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDao.clearData();
        authDao.clearData();
        registerService = new RegisterService(userDao, authDao);

    }

    @Test
    public void validRegister() throws ResponseException, DataAccessException {
        registerRequest = new RegisterRequest("myuser", "wordpass", "hello@mail.com");
        RegisterResponse registerResponse = registerService.register(registerRequest);
        assertEquals(registerRequest.username(), registerResponse.username());
    }

    @Test
    public void userAlreadyExists() throws DataAccessException {
        existingUser = new UserData("username", "secretpassword", "hey@yahoo.com");
        userDao.addUser(existingUser);

        registerRequest = new RegisterRequest("username", "bettersecret", "hey@yahoo.com");
        assertThrows(ResponseException.class, () -> registerService.register(registerRequest));

    }
}
