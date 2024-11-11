package ChessClients;

import server.ServerFacade;

import java.util.Arrays;

public class GameClient {
    private final ServerFacade server;
    public GameClient(ServerFacade server, String Auth, Integer gameID) {
        this.server = server;
//        this.notificationHandler = notificationHandler;
    }



    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "move" -> "make a move";
                case "print" -> "print the board";
                case "quit" -> "GOODBYE";
                default -> "HELP";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }

}
