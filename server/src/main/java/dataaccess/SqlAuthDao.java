package dataaccess;

import model.AuthData;
import server.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SqlAuthDao extends SqlDao implements AuthDao {

    public SqlAuthDao() {
        try {
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAuthData(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO authRecord (authToken, authDataJson) VALUES (?, ?)";
        String json = Server.GSON.toJson(authData);
        executeUpdate(statement, authData.authToken(), json);
    }

    @Override
    public void removeAuthData(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authRecord WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authDataJson FROM authRecord WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Server.GSON.fromJson(rs.getString("authDataJson"), AuthData.class);
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
        var statement = "TRUNCATE authRecord";
        executeUpdate(statement);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authRecord (
              authToken VARCHAR(128) NOT NULL PRIMARY KEY,
              authDataJson JSON NOT NULL,
              INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
