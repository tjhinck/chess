package dataaccess;

import model.UserData;

public class SqlUserDao implements UserDao{
    @Override
    public void addUser(UserData userData) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearData() throws DataAccessException {

    }
}
