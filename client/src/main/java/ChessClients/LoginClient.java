package ChessClients;

import exception.ResponseException;
import model.GameData;
import server.ServerFacade;

import java.util.Arrays;

public class LoginClient {
    private final String auth;
    private final String serverUrl;
    private final ServerFacade server;
    public LoginClient(String serverUrl, String auth) {
        server = new ServerFacade(serverUrl);
        this.auth = auth;
        this.serverUrl = serverUrl;
//        this.notificationHandler = notificationHandler;
    }



    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "list" -> returnList(auth);
                case "join" -> "game joined successfully";
                case "create" -> "create game here";
                case "logout" -> "GOODBYE";
                default -> "HELP";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }


    public String returnList(String auth) throws ResponseException {
        GameData[] retrievedList = server.listGames(auth);


    }

}
