package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDao implements AuthDao{
    final private HashMap<String, AuthData> authRecord = new HashMap<>();

    @Override
    public void addAuthData(AuthData authData)  {
        authRecord.put(authData.authToken(), authData);
    }

    @Override
    public void removeAuthData(String authToken) throws DataAccessException {
        authRecord.remove(authToken);
    }

    @Override
    public AuthData getAuthData(String authToken) {
        return authRecord.get(authToken);
    }

    @Override
    public void clearData() throws DataAccessException {
        authRecord.clear();
    }
}
