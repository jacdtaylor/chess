package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
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
    public void clear() {

    }

    @Override
    public AuthData getAuth(String auth) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public void createAuth(AuthData data) {

    }

    @Override
    public Boolean confirmAuthToken(String authToken) {
        return null;
    }

    @Override
    public String getUserFromAuth(String auth) {
        return "";
    }
}
