package dataaccess;

import model.GameData;
import model.GameDataDto;
import server.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameDao extends SqlDao implements GameDao {

    public SqlGameDao() {
        try {
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, gameDataJson) VALUES (?, ?)";
        String json = Server.GSON.toJson(gameData);
        executeUpdate(statement, gameData.gameID(), json);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameDataJson FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Server.GSON.fromJson(rs.getString("gameDataJson"), GameData.class);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("unable to get data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public Collection<GameDataDto> listGames() throws DataAccessException {
        Collection<GameDataDto> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameDataJson FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(Server.GSON.fromJson(rs.getString("gameDataJson"), GameDataDto.class));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return games;
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        var statement = "UPDATE games SET gameDataJson=? WHERE gameID=?";
        String json = Server.GSON.toJson(updatedGame, GameData.class);
        executeUpdate(statement, json, updatedGame.gameID());
    }

    @Override
    public void clearData() throws DataAccessException {
        var statement = "TRUNCATE games";
        executeUpdate(statement);
    }

    // todo: you can make your tables and queries more efficient
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              gameID int NOT NULL PRIMARY KEY,
              gameDataJson JSON NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
