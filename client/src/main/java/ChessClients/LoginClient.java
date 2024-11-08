package ChessClients;

import exception.ResponseException;
import model.GameData;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Collection;

public class LoginClient {
    private final String auth;
    private final ServerFacade server;
    public LoginClient(ServerFacade server, String auth) {
        this.server = server;
        this.auth = auth;
//        this.notificationHandler = notificationHandler;
    }



    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "list" -> returnList();
                case "join" -> "game joined successfully";
                case "create" -> createGame(params);
                case "logout" -> "GOODBYE";
                default -> "HELP";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }


    public String returnList() throws ResponseException {
        Collection<GameData> retrievedList = server.listGames(auth);
        return retrievedList.toString();
    }


    public String createGame(String... params) throws ResponseException {
        server.createGame(params[0],auth);
        return "Game Created Successfully";

    }

}
