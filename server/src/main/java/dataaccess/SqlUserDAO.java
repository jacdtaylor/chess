package dataaccess;

import Exceptions.DataAccessException;
import Exceptions.InvalidCredentialException;
import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.configureDatabase;
import static dataaccess.DatabaseManager.executeUpdate;


public class SqlUserDAO implements UserDAO{



    public SqlUserDAO()  {
        try {
            String[] createStatements = {
                    """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            configureDatabase(createStatements);} catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }




    private UserData readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return  new Gson().fromJson(json, UserData.class);
    }


        @Override
    public UserData getUser(String username) throws DataAccessException {
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "SELECT username, json FROM user WHERE username=?";
                try (var ps = conn.prepareStatement(statement)) {
                    ps.setString(1, username);
                    try (var rs = ps.executeQuery()) {
                        if (rs.next()) {
                            if (readUser(rs)==null) {throw new DataAccessException("Username not found");}
                            return readUser(rs);
                        }
                    }
                }
            } catch (Exception e) {
                throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
            }
            return null;
        }



    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, json) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        UserData newUser = new UserData(user.username(),hashedPassword);
        var json = new Gson().toJson(newUser);


        var id = executeUpdate(statement, user.username(), hashedPassword, json);
    }

    @Override
    public boolean confirmUsername(String username) throws DataAccessException {
        return getUser(username) != null;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);

    }

    @Override
    public boolean validateCredentials(UserData user) throws DataAccessException, InvalidCredentialException {

        UserData userData = getUser(user.username());
        if (userData == null) {throw new DataAccessException("Username does not exist");}
        if (user.password() == null) {throw new InvalidCredentialException("Password does not exist");}

        return BCrypt.checkpw(user.password(), userData.password());
    }
}
