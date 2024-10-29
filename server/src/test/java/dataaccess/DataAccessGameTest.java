package dataaccess;


import exception.DataAccessException;
import chess.*;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

public class DataAccessGameTest {

    static GameDAO gameDAO;




    @BeforeAll
    static void init() {
        gameDAO = new SqlGameDAO();}


    @BeforeEach
    void clearGame() throws DataAccessException {
        gameDAO.clear();}


    @Test
    @DisplayName("Clear Game")
    void clearGameTest() throws DataAccessException {
        GameData game = new GameData(1,null,null,"name", null);
        gameDAO.createGame(game);
        gameDAO.clear();

        assertNull(gameDAO.getGame(1));
    }


    @Test
    @DisplayName("Get Game +")
    void getGamePos() throws DataAccessException {
        GameData game = new GameData(1,null,null,"name", null);
        gameDAO.createGame(game);
        assertEquals(game, gameDAO.getGame(1));
    }

    @Test
    @DisplayName("Get Game -")
    void getGameNeg() throws DataAccessException {
        assertNull(gameDAO.getGame(1));
    }

    @Test
    @DisplayName("Create Game +")
    void createGamePos() throws DataAccessException {
        GameData game = new GameData(1,null,null,"name", null);
        gameDAO.createGame(game);
        assertEquals(game, gameDAO.getGame(1));
    }

    @Test
    @DisplayName("Create Game -")
    void createGameNeg() throws DataAccessException {
        GameData game = new GameData(1,null,null,"name", null);
        gameDAO.createGame(game);
        assertThrows(Exception.class, ()->{gameDAO.createGame(game);
        });
    }


    @Test
    @DisplayName("List Game +")
    void listGamePos() throws DataAccessException {
        GameData sample1 = new GameData(1,null,null,"sample1", null);
        GameData sample2 = new GameData(2,null,null,"sample2", null);
        GameData sample3 = new GameData(3,null,null,"sample3", null);
        Set<GameData> testList = new HashSet<>();
        testList.add(sample1);
        testList.add(sample2);
        testList.add(sample3);

        gameDAO.createGame(sample1);
        gameDAO.createGame(sample2);
        gameDAO.createGame(sample3);
        Set<GameData> returnedList = new HashSet<>(gameDAO.listGames());
        assertEquals(testList, returnedList);};

    @Test
    @DisplayName("List Game -")
    void listGameNeg() throws DataAccessException {
        Set<GameData> testList = new HashSet<>();
        Set<GameData> returnedList = new HashSet<>(gameDAO.listGames());
        assertEquals(testList, returnedList);
    };


    @Test
    @DisplayName("Update Game +")
    void updateGamePos() throws DataAccessException {
        ChessGame game = new ChessGame();
    GameData originalGame = new GameData(1,null,null,
            "sample1", game);
        gameDAO.createGame(originalGame);
        try {
            game.makeMove(new ChessMove(new ChessPosition(2,2), new ChessPosition(3,2), null));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        GameData newGame = new GameData(1,"homie","bro",
                "sample1", game);
        gameDAO.updateGame(newGame);

        assertEquals(newGame.game().toString(), gameDAO.getGame(1).game().toString());


    };

    @Test
    @DisplayName("Update Game -")
    void updateGameNeg() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData originalGame = new GameData(1,null,null,
                "sample1", game);

        assertThrows(Exception.class, ()->{gameDAO.updateGame(originalGame);});



    };
    }



//    void updateGame(GameData game) throws DataAccessException;


