package dataaccess;

import model.AuthData;

public class SqlAuthDao implements AuthDao{
    @Override
    public void addAuthData(AuthData authData) throws DataAccessException {

    }

    @Override
    public void removeAuthData(String authToken) throws DataAccessException {

    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void clearData() throws DataAccessException {

    }
}
