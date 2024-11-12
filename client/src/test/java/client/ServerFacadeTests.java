package client;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameReq;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import utility.validUUID;

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

    @AfterAll
    static void stopServer() throws ResponseException {
        facade.clearAll();
        server.stop();
    }


    @Test
    public void registerTestPos() throws ResponseException {
        UserData testUser = new UserData("username", "pass");
        AuthData auth = facade.registerUser(testUser);
        Assertions.assertTrue(validUUID.isValidUUID(auth.authToken()));
    }

    @Test
    public void registerTestNeg() throws ResponseException {
        UserData testUser = new UserData("username", "pass");
        facade.registerUser(testUser);
        Assertions.assertThrows(Exception.class, () -> {
            facade.registerUser(testUser);
        });
    }


    @Test
    public void loginTestPos() throws ResponseException {
        UserData testUser = new UserData("username", "pass");
        AuthData auth = facade.registerUser(testUser);
        facade.logoutUser(auth.authToken());
        AuthData auth2 = facade.loginUser(testUser);
        Assertions.assertTrue(validUUID.isValidUUID(auth2.authToken()));
    }

    @Test
    public void loginTestNeg() throws ResponseException {
        UserData testUser = new UserData("username", "pass");
        Assertions.assertThrows(Exception.class, () -> {
            facade.loginUser(testUser);
        });}

    @Test
    public void logoutTestPos() throws ResponseException {
        UserData testUser = new UserData("username", "pass");
        AuthData auth = facade.registerUser(testUser);

        Assertions.assertDoesNotThrow(
                ()-> {facade.logoutUser(auth.authToken());}
            );}


}







//public void logoutUser(String authToken) throws ResponseException {
//    var path = "/session";
//    this.makeRequest("DELETE", path, null, null, authToken);
//}
//
//
//public Collection<GameData> listGames(String auth) throws ResponseException {
//    var path = "/game";
//    record listGameDataResponse(Collection<GameData> games) {
//    }
//    var response = this.makeRequest("GET", path, null, listGameDataResponse.class, auth);
//    return response.games();
//}
//
//public int createGame(String gameName, String auth) throws ResponseException {
//    var path = "/game";
//    record createGameDataResponse(int id) {
//    }
//    GameData gameReq = new GameData(-999,null,null,gameName,null);
//    var response = this.makeRequest("POST", path, gameReq, createGameDataResponse.class, auth);
//    return response.id();
//}
//
//public void joinGame(JoinGameReq req, String auth) throws ResponseException {
//    var path = "/game";
//    this.makeRequest("PUT",path,req,null,auth);
//}
//
//private void clearAll() throws ResponseException {
//    var path = "/db";
//    this.makeRequest("DELETE",path,null,null,null);
//}