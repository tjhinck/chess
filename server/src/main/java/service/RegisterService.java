package service;

import response.ResponseException;
import org.mindrot.jbcrypt.BCrypt;
import request.RegisterRequest;
import response.RegisterResponse;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.UserDao;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {
    private final UserDao userDao;
    private final AuthDao authDao;

    public RegisterService(UserDao userDao, AuthDao authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public RegisterResponse register(RegisterRequest request) throws DataAccessException, ResponseException {
        UserData userCheck = userDao.getUser(request.username());
        if (userCheck == null) {
            String passwordHash = BCrypt.hashpw(request.password(), BCrypt.gensalt());
            UserData newUser = new UserData(request.username(), passwordHash, request.email());
            userDao.addUser(newUser);
            String newAuthToken = UUID.randomUUID().toString();
            AuthData newAuthData = new AuthData(newAuthToken, request.username());
            authDao.addAuthData(newAuthData);
            return new RegisterResponse(request.username(), newAuthToken);
        } else {
            throw new ResponseException(ResponseException.HttpCode.alreadyTaken, "Error: already taken");
        }
    }
}
