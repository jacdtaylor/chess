package dataaccess;

import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.HashSet;

public interface GameDAO {

    void createGame(GameData game);
    void clear();

    GameData getGame(int id) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    Collection<GameData> listGames();
}
