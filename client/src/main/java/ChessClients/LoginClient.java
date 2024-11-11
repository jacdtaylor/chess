package ChessClients;

import exception.ResponseException;
import model.GameData;
import model.JoinGameReq;
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
                case "join" -> joinGame(params);
                case "observe" -> observeGame();
                case "create" -> createGame(params);
                case "logout" -> logout();
                default -> "HELP";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }

    public String observeGame() {
        return "We be lookin'";
    }

    public String returnList() throws ResponseException {
        Collection<GameData> retrievedList = server.listGames(auth);
        return retrievedList.toString();
    }


    public String createGame(String... params) throws ResponseException {
        server.createGame(params[0],auth);
        return "Game Created Successfully";
    }

    public String joinGame(String... params) throws ResponseException {
        JoinGameReq req = new JoinGameReq(params[0],Integer.parseInt(params[1]));
        server.joinGame(req, auth);
        return "game joined";
    }

    public String logout() throws ResponseException {
        server.logoutUser(auth);
        return "GOODBYE";
    }

}
