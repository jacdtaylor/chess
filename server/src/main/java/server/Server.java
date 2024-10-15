package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.GameService;
import service.UserService;
import spark.*;

import javax.xml.crypto.Data;
import java.util.Map;

public class Server {
    private final GameService gameService;
    private final UserService userService;
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    public Server() {

        this.gameService = new GameService(userDAO, authDAO, gameDAO);
        this.userService = new UserService(userDAO, authDAO);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearDB);
        Spark.post("/user", this::registerUser);
        Spark.post("/session",this::loginUser);
        Spark.delete("/session",this::logout);
        Spark.get("/game",this::listGames);
        Spark.post("/game", this::createGame);
        Spark.post("/game", this::joinGame);

        Spark.exception(DataAccessException.class, this::dataAccessHandler);
        Spark.exception(UnauthorizationException.class, this::unauthorizationHandler);
        Spark.exception(GameManagerError.class, this::gameTakenHandler);
        Spark.exception(Exception.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

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

    private Object clearDB(Request req, Response res) {
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

    private Object joinGame(Request req, Response res) {
        String auth = req.headers("authorization");
        Object user = new Gson().fromJson(req.body(), Object.class);
        gameService.joinGame(auth, user["PlayerColor"],user["GameID"])
    }


    private Object listGames(Request req, Response res) {
        String auth = req.headers("authorization");
        var list = gameService.gameList(auth).toArray();
        return new Gson().toJson(Map.of("games", list));
    }

    private Object createGame(Request req, Response res) throws UnauthorizationException{
        String auth = req.headers("authorization");
        GameData body = new Gson().fromJson(req.body(), GameData.class);
        int newGameID = gameService.createGame(auth, body.gameName());

        return new Gson().toJson(Map.of("GameID", "Test"));

//        String auth = "testAuth";
//        return new Gson().toJson(Map.of("GameID", newGameID));
    }


    private Object unauthorizationHandler(UnauthorizationException e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(401);
        res.body(body);
        return body;



    }

    private Object gameTakenHandler(GameManagerError e, Request req, Response res) {
        res.status(403);
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.body(body);
        return body;
    }

    private Object dataAccessHandler(DataAccessException e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(401);
        res.body(body);
        return body;

    }

    private Object exceptionHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(400);
        res.body(body);
        return body;


    }
}





