package ChessClients;

import java.util.Arrays;

public class PreLoginClient {

    private final String serverUrl;
    public PreLoginClient(String serverUrl) {
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
            case "login" -> Login(params);
            case "register" -> "REGISTER FUNCTIONALITY";
            case "quit" -> "GOODBYE";
            default -> "HELP";
        };
    } catch (Exception ex) {
        return ex.getMessage();
    }}


    public String Login(String... params) {

    }
}
