package chessClients;


import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import utility.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

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
            case "login" -> login(params);
            case "register" -> register(params);
            case "quit" -> "GOODBYE";
            case "clear" -> clearDB();
            default -> getHelp();
        };
    } catch (Exception ex) {
        return ex.getMessage();
    }}


    public String login(String... params) throws Exception {
        if (params.length > 2 ) {return SET_TEXT_COLOR_RED + "TOO MANY ARGUMENTS" + RESET_TEXT_COLOR + "\n";
        }
        try {
        UserData user = new UserData(params[0],params[1]);
        AuthData result = server.loginUser(user);
        return result.authToken();}
        catch (ResponseException ex) {
            return  SET_TEXT_COLOR_RED + "INCORRECT CREDENTIALS" + RESET_TEXT_COLOR + "\n";
        } catch (Exception ex) {
            return SET_TEXT_COLOR_RED +"PLEASE ENTER USERNAME AND PASSWORD" + RESET_TEXT_COLOR + "\n";
        }
    }

    public String register(String... params) {
        if (params.length > 2 ) {return SET_TEXT_COLOR_RED + "TOO MANY ARGUMENTS" + RESET_TEXT_COLOR + "\n";
        }
        try {UserData user = new UserData(params[0],params[1]);
        AuthData result = null;

            result = server.registerUser(user);
            return result.authToken();
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED + "USER EXISTS" + RESET_TEXT_COLOR + "\n";
        }  catch (Exception ex) {
        return SET_TEXT_COLOR_RED +"PLEASE ENTER USERNAME AND PASSWORD" + RESET_TEXT_COLOR + "\n";
    }


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

    public String clearDB() throws ResponseException {
        server.clearAll();
        return "CLEARED\n";
    }
}
