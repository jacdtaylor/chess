package ChessClients;

import java.util.Arrays;

public class LoginClient {

    private final String serverUrl;
    public LoginClient(String serverUrl) {
//        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
//        this.notificationHandler = notificationHandler;
    }



    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "list" -> "list games here";
                case "join" -> "game joined successfully";
                case "create" -> "create game here";
                case "logout" -> "GOODBYE";
                default -> "HELP";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }

}
