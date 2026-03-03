package service;

import dataaccess.AuthDao;
import dataaccess.DataAccessException;

public class LogoutService {
    private final AuthDao authDao;

    public LogoutService(AuthDao authDao){
        this.authDao = authDao;
    }

    public void logout(String authToken) throws DataAccessException {
        authDao.removeAuthData(authToken);
    }
}
