package server;

import exception.DataAccessException;
import exception.GameManagerError;
import exception.UnauthorizationException;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.JoinGameReq;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Map;

public class Server {
    static GameService gameService;
    static UserService userService;

    private final WebSocketHandler webSocketHandler;
    public Server() {
        UserDAO userDAO = new SqlUserDAO();
        AuthDAO authDAO = new SqlAuthDAO();
        GameDAO gameDAO = new SqlGameDAO();


        webSocketHandler = new WebSocketHandler();
        gameService = new GameService(userDAO, authDAO, gameDAO);
        userService = new UserService(userDAO, authDAO);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.

        Spark.webSocket("/ws", webSocketHandler);

        Spark.delete("/db", this::clearDB);
        Spark.post("/user", this::registerUser);
        Spark.put("/name", this::getUser);
        Spark.post("/session",this::loginUser);
        Spark.delete("/session",this::logout);
        Spark.get("/game",this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.put("/update", this::updateGame);
        Spark.put("/current", this::getGame);

        Spark.exception(DataAccessException.class, this::dataAccessHandler);
        Spark.exception(GameManagerError.class, this::takenHandler);
        Spark.exception(Exception.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public int port() {
        return Spark.port();
    }

    private Object clearDB(Request req, Response res) throws DataAccessException {
        gameService.clearAll();
        res.status(200);
        return "";
    }

    private Object registerUser(Request req, Response res) throws DataAccessException {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        AuthData auth = userService.register(user);
        res.status(200);
        return new Gson().toJson(auth);
    }

    private Object loginUser(Request req, Response res) throws DataAccessException {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        AuthData auth = userService.login(user);
        res.status(200);
        return new Gson().toJson(auth);

    }

    private Object logout(Request req, Response res) throws DataAccessException {
        String auth = req.headers("authorization");
//        AuthData auth = new Gson().fromJson(req.body(), AuthData.class);
        userService.logout(auth);
        res.status(200);
        return "";
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        String auth = req.headers("authorization");
        JoinGameReq gameData = new Gson().fromJson(req.body(), JoinGameReq.class);
        gameService.joinGame(auth, gameData);
        res.status(200);
        return "";

    }


    private Object listGames(Request req, Response res) throws DataAccessException {
        String auth = req.headers("authorization");
        var list = gameService.gameList(auth).toArray();
        res.status(200);


        return new Gson().toJson(Map.of("games", list));
    }

    private Object createGame(Request req, Response res) throws UnauthorizationException, DataAccessException {

        String auth = req.headers("authorization");
        GameData body = new Gson().fromJson(req.body(), GameData.class);
        res.status(200);
        int newGameID = gameService.createGame(auth, body.gameName());
        return new Gson().toJson(Map.of("gameID", newGameID));
    }

    private Object updateGame(Request req, Response res) throws DataAccessException {
        String auth = req.headers("authorization");
        GameData body = new Gson().fromJson(req.body(), GameData.class);
        res.status(200);
        GameData updatedGame = gameService.updateGame(body);
        return new Gson().toJson(updatedGame);

    }

    private Object getGame(Request req, Response res) throws DataAccessException {

        GameData currentGame = gameService.getGameData(Integer.parseInt(req.body()));
        res.status(200);
        return new Gson().toJson(currentGame);
    }

    private Object getUser(Request req, Response res) throws DataAccessException {
        String auth = req.headers("authorization");
        String username = userService.getUser(auth);
        res.status(200);
        return username;
    }





    private Object takenHandler(GameManagerError e, Request req, Response res) {
        res.status(403);
        var body = new Gson().toJson(Map.of("message", String.format("ErrorMessage: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.body(body);
        return body;
    }

    private Object dataAccessHandler(DataAccessException e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("ErrorMessage: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(401);
        res.body(body);
        return body;

    }

    private Object exceptionHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("ErrorMessage: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(400);
        res.body(body);
        return body;


    }
}





