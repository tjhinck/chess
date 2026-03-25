package service;

import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import exception.ResponseException;

public class LogoutService {
    private final AuthDao authDao;

    public LogoutService(AuthDao authDao){
        this.authDao = authDao;
    }

    public void logout(String authToken) throws DataAccessException, ResponseException {
        if (authDao.getAuthData(authToken) != null) {
            authDao.removeAuthData(authToken);
        } else {
            throw new ResponseException(ResponseException.HttpCode.badRequest, "Error: bad request");
        }
    }
}
