package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDao {

    void addUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void addAuthData(AuthData authData) throws DataAccessException;

    AuthData getAuthData(String authToken) throws  DataAccessException;

    void clearData() throws DataAccessException;


}
