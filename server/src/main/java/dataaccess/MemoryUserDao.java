package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDao implements UserDao{
    final private HashMap<String, UserData> users = new HashMap<>();
    final private HashMap<String, AuthData> authRecord = new HashMap<>();

    @Override
    public void addUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void addAuthData(AuthData authData)  {
        authRecord.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuthData(String authToken) {
        return authRecord.get(authToken);
    }

    @Override
    public void clearData() throws DataAccessException {
        users.clear();
        authRecord.clear();
    }
}
