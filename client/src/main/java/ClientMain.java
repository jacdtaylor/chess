
import ChessRepl.ChessRepl;
import server.ServerFacade;

public class ClientMain {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new ChessRepl(new ServerFacade(serverUrl), null).run("prelogin");
    }

}