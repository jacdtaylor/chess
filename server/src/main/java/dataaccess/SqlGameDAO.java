package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.JoinGameReq;
import model.UserData;
import chess.ChessGame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlGameDAO implements GameDAO{


    public SqlGameDAO(){
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL,
              `name` varchar(256) NOT NULL,
              `chessGame` varchar(256) NOT NULL,
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
    var statement = "INSERT INTO game (id, name, chessGame, json) VALUES (?, ?, ?, ?)";
    var chessjson = new Gson().toJson(game.game());
    var json = new Gson().toJson(game);
    executeUpdate(statement,game.gameID(), game.gameName(),chessjson ,json);
    }

    @Override
    public void clear() {
        var statement = "DELETE FROM game";
        executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, GameData.class);
    }

    @Override
    public GameData getGame(int id) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json, chessGame FROM game WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
//                        return readGame(rs);
                        GameData oldData = readGame(rs);
                        String stringChessGame = rs.getString("chessGame");
                        ChessGame newChessGame = new Gson().fromJson(stringChessGame, ChessGame.class);
                        return new GameData(oldData.gameID(),oldData.whiteUsername(),
                                oldData.blackUsername(),oldData.gameName(), newChessGame);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }



    public void deleteGame(GameData game)  {
        var statement = "DELETE FROM game WHERE id=?";
        executeUpdate(statement, game.gameID());
    }


    @Override
    public void updateGame(GameData game)  {
        deleteGame(game);
        createGame(game);
    }

    @Override
    public Collection<GameData> listGames() {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    private void executeUpdate(String statement, Object... params)  {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case UserData p -> ps.setString(i + 1, p.toString());
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    rs.getInt(1);
                }

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Database Error");
        }}
}

