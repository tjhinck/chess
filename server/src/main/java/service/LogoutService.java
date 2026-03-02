package service;

import Response.ResponseException;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import model.AuthData;

public class LogoutService {
    private final AuthDao authDao;

    public LogoutService(AuthDao authDao){
        this.authDao = authDao;
    }

    public void logout(String authToken) throws DataAccessException, ResponseException {
        authDao.removeAuthData(authToken);

//        AuthData foundAuth = authDao.getAuthData(authToken);
//        if (foundAuth != null){
//            authDao.removeAuthData(authToken);
//        } else {
//            throw new ResponseException(ResponseException.httpCode.unauthorized, "Error: unauthorized");
//        }
    }
}
