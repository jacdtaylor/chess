package dataaccess;

import Exceptions.DataAccessException;
import com.google.gson.Gson;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.configureDatabase;
import static dataaccess.DatabaseManager.executeUpdate;

public class SqlAuthDAO implements AuthDAO{


    public SqlAuthDAO()  {
        try {
            String[] createStatements = {
                    """
            CREATE TABLE IF NOT EXISTS  auth (
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }





    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }


    private AuthData readAuth(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, AuthData.class);
    }



    @Override
    public AuthData getAuth(String auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,auth);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR");
        }
        throw new DataAccessException("ERROR");
    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {
        try {getAuth(auth.authToken());} catch (Exception e) {throw new DataAccessException("Invalid Auth");}
        var statement = "DELETE FROM auth WHERE authToken=?";{
            executeUpdate(statement, auth.authToken());
        }
    }

    @Override
    public void createAuth(AuthData data) {
        var statement = "INSERT INTO auth (username, authToken, json) VALUES (?, ?, ?)";
        var json = new Gson().toJson(data);
        var id = executeUpdate(statement, data.username(), data.authToken(), json);

    }

    @Override
    public Boolean confirmAuthToken(String authToken) throws DataAccessException {
        return getAuth(authToken) != null;
    }

    @Override
    public String getUserFromAuth(String auth) throws DataAccessException {
        AuthData data = null;
        data = getAuth(auth);
        return data.username();
    }
}
