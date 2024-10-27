package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class SqlGameDAO implements GameDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `whiteUsername` varchar(256) NOT NULL,
              'blackUsername' varchar(256) NOT NULL
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public void createGame(GameData game) {

    }

    @Override
    public void clear() {

    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }
}

