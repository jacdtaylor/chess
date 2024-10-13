package dataaccess;

import model.GameData;

import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{

    HashSet<GameData> storedGameData = new HashSet<GameData>();

    @Override
    public GameData getGame(int ID) throws DataAccessException {
        for (GameData game : storedGameData) {
            if (game.gameID() == ID) {
                return game;
            }
        }
        throw new DataAccessException("Game ID not found: " + ID);

    }


    @Override
    public void updateGame(GameData game) throws DataAccessException {
        GameData targetGame = getGame(game.gameID());
        storedGameData.remove(targetGame);
        storedGameData.add(game);

    }

    @Override
    public HashSet<GameData> listGames() {
        return storedGameData;
    }
    @Override
    public void createGame(GameData game) {
        storedGameData.add(game);
    }
    @Override
    public void clear() {storedGameData = new HashSet<GameData>();}
}
