package service;

import dataaccess.AuthDao;
import dataaccess.DataAccessException;

public class AuthenticationService {
    private final AuthDao authDao;

    public AuthenticationService(AuthDao authDao){
        this.authDao = authDao;
    }

    public boolean isValidToken(String authToken) throws DataAccessException {
        if (authToken != null && authDao.getAuthData(authToken) != null){
            return true;
        }
        return false;
    }
}
