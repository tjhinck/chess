package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTests {

    private static UserDao userDao;

    @BeforeAll
    public static void init(){
        userDao = new SqlUserDao();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDao.clearData();
    }

    @Test
    public void addUser() throws DataAccessException {
        userDao.addUser(new UserData("user", "hashed", "mail"));
        UserData user = userDao.getUser("user");
        assertEquals(user.email(), "mail");
    }

    @Test
    public void addAndClose() throws DataAccessException {
        userDao.addUser(new UserData("user", "hashed", "mail"));
        userDao = null;
        userDao = new SqlUserDao();
        assertNotNull(userDao.getUser("user"));
    }

    @Test
    public void notFound() throws DataAccessException{
        userDao.addUser(new UserData("user", "hashed", "mail"));
        assertNull(userDao.getUser("nobody"));
    }

    @Test
    public void closeAndGet() throws DataAccessException {
        userDao.addUser(new UserData("user", "hashed", "mail"));
        userDao = null;
        userDao = new SqlUserDao();
        UserData user = userDao.getUser("user");
        assertEquals(user.email(), "mail");
    }

    @Test
    public void clear() throws DataAccessException {
        userDao.addUser(new UserData("user", "hashed", "mail"));
        userDao.clearData();
        assertNull(userDao.getUser("user"));
    }
}
