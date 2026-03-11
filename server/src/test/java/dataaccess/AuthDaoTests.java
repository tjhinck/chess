package dataaccess;


import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AuthDaoTests {

    private static AuthDao authDao;

    @BeforeAll
    public static void init(){
        authDao = new SqlAuthDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        authDao.clearData();
    }

    @Test
    public void addAuth() throws DataAccessException {
        authDao.addAuthData(new AuthData("goodtoken", "username"));
        AuthData auth =  authDao.getAuthData("goodtoken");
        assertEquals(auth.username(), "username");
    }

    @Test
    public void invalidAuth() throws DataAccessException{
        authDao.addAuthData(new AuthData("goodtoken", "username"));
        assertNull(authDao.getAuthData("nobody"));
    }

    @Test
    public void removeAuth() throws DataAccessException {
        authDao.addAuthData(new AuthData("goodtoken", "username"));
        authDao.removeAuthData("goodtoken");
        assertNull(authDao.getAuthData("goodtoken"));
    }



}

