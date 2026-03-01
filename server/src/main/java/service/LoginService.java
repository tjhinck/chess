package service;

import Request.LoginRequest;
import Response.LoginResponse;
import Response.ResponseException;
import dataaccess.DataAccessException;
import dataaccess.UserDao;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;


public class LoginService {
    private UserDao userDao;

    public LoginService(UserDao userDao) {
        this.userDao = userDao;
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException, ResponseException {
        UserData foundUser = userDao.getUser(request.username());
        if (foundUser != null && Objects.equals(foundUser.password(), request.password())){
            String newAuthToken = UUID.randomUUID().toString();
            AuthData newAuthData = new AuthData(newAuthToken, request.username());
            userDao.addAuthData(newAuthData);
            return new LoginResponse(request.username(), newAuthToken);
        } else {
            throw new ResponseException(ResponseException.httpCode.unauthorized, "Error: unauthorized");
        }
    }
}
