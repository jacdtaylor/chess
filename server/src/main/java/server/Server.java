package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Map;

public class Server {
    private final GameService gameService;
    private final UserService userService;

    public Server() {
        this.gameService = new GameService(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        this.userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearDB);
        Spark.post("/user", this::registerUser);
        Spark.post("/session",this::loginUser);
        Spark.delete("/session",this::logoutUser);
//        Spark.get("/game",this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.post("/game", this::joinGame);


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
        res.status(204);
        return "";
    }

    private Object registerUser(Request req, Response res) throws DataAccessException {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        AuthData auth = userService.register(user);
        return new Gson().toJson(auth);
    }

    private Object loginUser(Request req, Response res) throws DataAccessException {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        AuthData auth = userService.login(user);
        return new Gson().toJson(auth);

    }

    private Object logoutUser(Request req, Response res) throws DataAccessException {
        AuthData auth = new Gson().fromJson(req.body(), AuthData.class);
        userService.logout(auth);
        return "";
    }

//    private Object listGames(Request req, Response res) {
//        String auth = res.header;
//        var list = gameService.gameList(auth).toArray();
//        return new Gson().toJson(Map.of("games", list));
//    }

//    private Object createGame(Request req, Response res) {}

}


