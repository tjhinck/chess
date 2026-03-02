package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDao implements UserDao{
    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void addUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void clearData() throws DataAccessException {
        users.clear();
    }
}
