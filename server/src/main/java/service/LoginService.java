package service;

import request.LoginRequest;
import response.LoginResponse;
import response.ResponseException;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.UserDao;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;


public class LoginService {
    private final UserDao userDao;
    private final AuthDao authDao;

    public LoginService(UserDao userDao, AuthDao authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException, ResponseException {
        UserData foundUser = userDao.getUser(request.username());
        if (foundUser != null && Objects.equals(foundUser.password(), request.password())){
            String newAuthToken = UUID.randomUUID().toString();
            AuthData newAuthData = new AuthData(newAuthToken, request.username());
            authDao.addAuthData(newAuthData);
            return new LoginResponse(request.username(), newAuthToken);
        } else {
            throw new ResponseException(ResponseException.HttpCode.unauthorized, "Error: unauthorized");
        }
    }
}
