package dataaccess;

import exception.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    void createGame(GameData game);
    void clear();

    GameData getGame(int id) throws DataAccessException;
    GameData updateGame(GameData game) throws DataAccessException;


    Collection<GameData> listGames();
}
