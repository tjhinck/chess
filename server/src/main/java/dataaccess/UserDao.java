package dataaccess;

import model.UserData;

public interface UserDao {

    void addUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clearData() throws DataAccessException;


}
