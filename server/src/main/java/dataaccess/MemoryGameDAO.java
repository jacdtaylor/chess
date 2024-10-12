package dataaccess;

import model.GameData;

import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{

    HashSet<GameData> storedGameData = new HashSet<GameData>();


    public GameData getGame(int ID) throws DataAccessException {
        for (GameData game : storedGameData) {
            if (game.gameID() == ID) {
                return game;
            }
        }
        throw new DataAccessException("Game ID not found: " + ID);

    }

    public HashSet<GameData> listGames() {
        return storedGameData;
    }

    public void createGame(GameData game) {
        storedGameData.add(game);
    }

    public void clear() {storedGameData = new HashSet<GameData>();}
}
