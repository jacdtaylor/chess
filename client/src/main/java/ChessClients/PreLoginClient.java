package ChessClients;

import exception.ResponseException;
import model.AuthData;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class PreLoginClient {
    private final ServerFacade server;
    public PreLoginClient(ServerFacade server) {
        this.server = server;
//        this.notificationHandler = notificationHandler;
    }



    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
        return switch (cmd) {
            case "login" -> Login(params);
            case "register" -> Register(params);
            case "quit" -> "GOODBYE";
            default -> getHelp();
        };
    } catch (Exception ex) {
        return ex.getMessage();
    }}


    public String Login(String... params) throws Exception {
        UserData user = new UserData(params[0],params[1]);
        AuthData result = server.loginUser(user);
        return result.authToken();
    }

    public String Register(String... params) throws ResponseException {
        UserData user = new UserData(params[0],params[1]);
        AuthData result = server.registerUser(user);
        return result.authToken();

    }
    public String getHelp() {
        return
               """
               register <USERNAME> <PASSWORD>
               login <USERNAME> <PASSWORD>
               help
               quit
               """
                ;
    }
}
