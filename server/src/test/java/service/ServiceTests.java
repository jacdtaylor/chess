package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.JoinGameReq;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    static GameService gameService;
    static UserService userService;
    static GameDAO gameDAO;
    static UserDAO userDAO;
    static AuthDAO authDAO;

    @BeforeAll
    static void init() {
       gameDAO = new MemoryGameDAO();
       userDAO = new MemoryUserDAO();
       authDAO = new MemoryAuthDAO();
       gameService = new GameService(userDAO,authDAO,gameDAO);
       userService = new UserService(userDAO,authDAO);



    }


    @BeforeEach
    void clearGame() {
        gameService.clearAll();
        AuthData authData = new AuthData("AuthToken","Username");
        authDAO.createAuth(authData);
    }

    @Test
    @DisplayName("Join Game Test")
    void joinGameWorking() throws DataAccessException {
        int ID = gameService.createGame("AuthToken","GameName");
        JoinGameReq newReq = new JoinGameReq("WHITE", ID);
        gameService.joinGame("AuthToken", newReq);
        GameData game = gameDAO.getGame(ID);
        Assertions.assertEquals(game.whiteUsername(),"Username");

    }

    @Test
    @DisplayName("Join Game Test")
    void joinGameBroken() throws DataAccessException {
        int ID = gameService.createGame("AuthToken","GameName");
        JoinGameReq newReq = new JoinGameReq("WHITE", ID);
        assertThrows(UnauthorizationException.class, ()->{gameService.joinGame("fakeToken", newReq);});
    }


    @Test
    @DisplayName("Create Game Test")
    void createGameWorking() throws DataAccessException {
        int ID = gameService.createGame("AuthToken","GameName");
        GameData madeGame = gameDAO.getGame(ID);
        assertEquals(ID, madeGame.gameID());
    }

    @Test
    @DisplayName("Create Game Test")
    void createGameBroken() throws DataAccessException {
        assertThrows(UnauthorizationException.class, () ->
        {
            gameService.createGame("fakeToken", "GameName");
        });
    }


    @Test
    @DisplayName("Game List Test")
    void gameListWorking() {
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
        Set<GameData> returnedList = new HashSet<>(gameService.gameList("AuthToken"));
        assertEquals(testList, returnedList);
    }

    @Test
    @DisplayName("Game List Test")
    void gameListBroken() {
        assertThrows(UnauthorizationException.class,() -> {gameService.gameList("fakeToken");});
    }


    @Test
    @DisplayName("Clear Test")
    void gameClearTest() {
        GameData game = new GameData(1,null,null,null,null);
        gameDAO.createGame(game);
        gameService.clearAll();
        assertThrows(UnauthorizationException.class,() -> {gameService.gameList("AuthToken");});
    }

}
