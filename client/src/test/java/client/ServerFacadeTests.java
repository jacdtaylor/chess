package client;

import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameReq;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import utility.ServerFacade;
import utility.ValidUUID;

import java.util.Collection;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
        facade.clearAll();
    }

    @AfterEach
    void clear() throws ResponseException {
        facade.clearAll();

    }

    @AfterAll
    static void stopServer() throws ResponseException {
        server.stop();
    }


    @Test
    public void registerTestPos() throws ResponseException {
        UserData testUser = new UserData("username", "pass", "mail");
        AuthData auth = facade.registerUser(testUser);
        Assertions.assertTrue(ValidUUID.isValidUUID(auth.authToken()));
    }

    @Test
    public void registerTestNeg() throws ResponseException {
        UserData testUser = new UserData("username", "pass", "mail");
        facade.registerUser(testUser);
        Assertions.assertThrows(Exception.class, () -> {
            facade.registerUser(testUser);
        });
    }


    @Test
    public void loginTestPos() throws ResponseException {
        UserData testUser = new UserData("username", "pass", "mail");
        AuthData auth = facade.registerUser(testUser);
        facade.logoutUser(auth.authToken());
        AuthData auth2 = facade.loginUser(testUser);
        Assertions.assertTrue(ValidUUID.isValidUUID(auth2.authToken()));
    }

    @Test
    public void loginTestNeg() throws ResponseException {
        UserData testUser = new UserData("username", "pass", "mail");
        Assertions.assertThrows(Exception.class, () -> {
            facade.loginUser(testUser);
        });}

    @Test
    public void logoutTestPos() throws ResponseException {
        UserData testUser = new UserData("username", "pass", "mail");
        AuthData auth = facade.registerUser(testUser);

        Assertions.assertDoesNotThrow(
                ()-> {facade.logoutUser(auth.authToken());}
            );}

    @Test
    public void logoutTestNeg() throws ResponseException {
        Assertions.assertThrows(Exception.class,
                ()-> {facade.logoutUser("FAKE AUTH TOKEN");}
        );}

    @Test
    public void listTestPos() throws ResponseException {

        UserData testUser = new UserData("username", "pass", "mail");
        AuthData auth = facade.registerUser(testUser);
        facade.createGame("TEST GAME", auth.authToken());
        Collection<GameData> result = facade.listGames(auth.authToken());
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    public void listTestNeg() throws ResponseException {
        Assertions.assertThrows(Exception.class, ()->{facade.listGames("FAKE AUTH");});
    }

    @Test
    public void createTestPos() throws ResponseException {
        UserData testUser = new UserData("username", "pass", "mail");
        AuthData auth = facade.registerUser(testUser);

        int id = facade.createGame("Game", auth.authToken());
        Assertions.assertEquals(0, id);
    }

    @Test
    public void createTestNeg() throws ResponseException {
        Assertions.assertThrows(Exception.class, ()->{facade.createGame("BAD EGG", "FAKE AUTH");});
    }

    @Test
    public void joinTestPos() throws ResponseException {
        UserData testUser = new UserData("username", "pass", "mail");
        AuthData auth = facade.registerUser(testUser);
        facade.createGame("Game", auth.authToken());
        Collection<GameData> list = facade.listGames(auth.authToken());
        GameData[] gameArray = list.toArray(new GameData[0]);
        int gameID = gameArray[0].gameID();
        JoinGameReq req = new JoinGameReq("WHITE", gameID);
        facade.joinGame(req, auth.authToken());
        Collection<GameData> list2 = facade.listGames(auth.authToken());
        GameData[] gameArray2 = list2.toArray(new GameData[0]);
        Assertions.assertEquals(gameArray2[0].whiteUsername(), "username");

    }
    @Test
    public void joinTestNeg() throws ResponseException {
        UserData testUser = new UserData("username", "pass", "mail");
        AuthData auth = facade.registerUser(testUser);
        facade.createGame("Game", auth.authToken());
        JoinGameReq req = new JoinGameReq("WHITE", -999);
        Assertions.assertThrows(Exception.class, ()->{facade.joinGame(req, auth.authToken());});

    }

    @Test
    public void clearTest() throws ResponseException {
        UserData testUser = new UserData("username", "pass", "mail");
        AuthData auth = facade.registerUser(testUser);
        facade.clearAll();
        Assertions.assertDoesNotThrow(()->{facade.registerUser(testUser);});
    }


}


