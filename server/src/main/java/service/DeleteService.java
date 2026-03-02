package service;

import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import dataaccess.UserDao;

public class DeleteService {
    private final UserDao userDao;
    private final AuthDao authDao;
    private final GameDao gameDao;

    public DeleteService(UserDao userDao, AuthDao authDao, GameDao gameDao){
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public void clearAll() throws DataAccessException {
        userDao.clearData();
        authDao.clearData();
        gameDao.clearData();
    }
}
