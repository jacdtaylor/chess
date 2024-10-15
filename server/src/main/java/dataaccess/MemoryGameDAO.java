package dataaccess;

import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{

    private final HashMap<String, GameData> gameDataHash = new HashMap<>();

    @Override
    public GameData getGame(int ID) throws NonexistantGame {
        GameData pulledGame = gameDataHash.get(Integer.toString(ID));
        if (pulledGame != null) {return pulledGame;}
        throw new NonexistantGame("Game ID not found: " + ID);

    }


    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (!gameDataHash.containsKey(Integer.toString(game.gameID()))) {
            throw new DataAccessException("Game does not exist");
        }
        gameDataHash.remove(Integer.toString(game.gameID()));
        gameDataHash.put(Integer.toString(game.gameID()), game);

    }

    @Override
    public Collection<GameData> listGames() {
        return gameDataHash.values();
    }
    @Override
    public void createGame(GameData game) {
        gameDataHash.put(Integer.toString(game.gameID()),game);
    }
    @Override
    public void clear() {gameDataHash.clear();}
}
