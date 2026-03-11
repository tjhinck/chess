package dataaccess;

import model.UserData;
import server.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SqlUserDao implements UserDao{

    public SqlUserDao() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO userData (username, userDataJson) VALUES (?, ?)";
        String json = Server.GSON.toJson(userData);
        executeUpdate(statement, userData.username(), json);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT userDataJson FROM userData WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Server.GSON.fromJson(rs.getString("userDataJson"), UserData.class);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("unable to get data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void clearData() throws DataAccessException {
        var statement = "TRUNCATE userData";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof UserData p) ps.setString(i + 1, p.toString());
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userData (
              username VARCHAR(64) NOT NULL PRIMARY KEY,
              userDataJson JSON NOT NULL,
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
