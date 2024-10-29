package dataaccess;

import exception.DataAccessException;
import exception.NonexistantGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    private final HashMap<String, GameData> gameDataHash = new HashMap<>();

    @Override
    public GameData getGame(int id) throws NonexistantGame {
        GameData pulledGame = gameDataHash.get(Integer.toString(id));
        if (pulledGame != null) {return pulledGame;}
        throw new NonexistantGame("Game ID not found: " + id);

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
