package service;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.JoinGameReq;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
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
    void clearGame() throws DataAccessException {
        gameService.clearAll();
        AuthData authData = new AuthData("AuthToken","Username");
        authDAO.createAuth(authData);
    }

    @Test
    @DisplayName("Join Game Test +")
    void joinGameWorking() throws DataAccessException {
        int id = gameService.createGame("AuthToken","GameName");
        JoinGameReq newReq = new JoinGameReq("WHITE", id);
        gameService.joinGame("AuthToken", newReq);
        GameData game = gameDAO.getGame(id);
        Assertions.assertEquals(game.whiteUsername(),"Username");

    }

    @Test
    @DisplayName("Join Game Test -")
    void joinGameBroken() throws DataAccessException {
        int id = gameService.createGame("AuthToken","GameName");
        JoinGameReq newReq = new JoinGameReq("WHITE", id);
        assertThrows(DataAccessException.class, ()->{gameService.joinGame("fakeToken", newReq);});
    }


    @Test
    @DisplayName("Create Game Test +")
    void createGameWorking() throws DataAccessException {
        int id = gameService.createGame("AuthToken","GameName");
        GameData madeGame = gameDAO.getGame(id);
        assertEquals(id, madeGame.gameID());
    }

    @Test
    @DisplayName("Create Game Test -")
    void createGameBroken() throws DataAccessException {
        assertThrows(DataAccessException.class, () ->
        {
            gameService.createGame("fakeToken", "GameName");
        });
    }


    @Test
    @DisplayName("Game List Test +")
    void gameListWorking() throws DataAccessException {
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
    @DisplayName("Game List Test -")
    void gameListBroken() {
        assertThrows(DataAccessException.class,() -> {gameService.gameList("fakeToken");});
    }


    @Test
    @DisplayName("Clear Test +")
    void gameClearTest() throws DataAccessException {
        GameData game = new GameData(1,null,null,null,null);
        gameDAO.createGame(game);
        gameService.clearAll();
        assertThrows(DataAccessException.class,() -> {gameService.gameList("AuthToken");});
    }

    @Test
    @DisplayName("Register Test +")
    void registerTestWorking() throws DataAccessException {
        UserData user = new UserData("Sample1", "P");
        userService.register(user);
        assertEquals(userDAO.getUser("Sample1"), user);
        }

    @Test
    @DisplayName("Register Test -")
    void registerTestBroken() throws DataAccessException {
        UserData user = new UserData("Sample1", "P");
        userService.register(user);
        assertThrows(GameManagerError.class, ()->{userService.register(user);});
    }


    @Test
    @DisplayName("Login Test +")
    void loginTestWorking() throws DataAccessException {
        UserData user = new UserData("Sample1", "P");
        AuthData token = userService.register(user);
        userService.logout(token.authToken());
        AuthData token2 = userService.login(user);
        assertEquals(authDAO.getUserFromAuth(token2.authToken()), "Sample1");
    }

    @Test
    @DisplayName("Login Test -")
    void loginTestBroken() throws DataAccessException {
        UserData user = new UserData("Sample1", "P");
        assertThrows(DataAccessException.class , ()->{ AuthData token2 = userService.login(user);
        ;});
    }


    @Test
    @DisplayName("Logout Test -")
    void logoutTestBroken() throws DataAccessException {
        assertThrows(DataAccessException.class , ()->{ userService.logout("fakeAuth");
            ;});
    }


    @Test
    @DisplayName("Logout Test +")
    void logoutTestWorking() throws DataAccessException {
        UserData user = new UserData("Sample1", "P");
        AuthData token = userService.register(user);
        userService.logout(token.authToken());

        assertThrows(DataAccessException.class , ()->{ authDAO.getAuth(token.authToken());
            ;});
    }




}
