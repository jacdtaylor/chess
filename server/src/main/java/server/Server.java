package server;

import org.eclipse.jetty.server.Authentication;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private final GameService gameService = new GameService();
    private final UserService userService = new UserService();



    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
