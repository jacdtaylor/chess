package dataaccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {

    void createGame(GameData game);
    void clear();

    GameData getGame(int ID) throws DataAccessException;

    HashSet<GameData> listGames();
}
