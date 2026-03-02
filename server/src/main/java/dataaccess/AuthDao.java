package dataaccess;

import model.AuthData;

public interface AuthDao {

    void addAuthData(AuthData authData) throws DataAccessException;

    void removeAuthData(String authToken) throws DataAccessException;

    AuthData getAuthData(String authToken) throws  DataAccessException;

    void clearData() throws DataAccessException;

}
