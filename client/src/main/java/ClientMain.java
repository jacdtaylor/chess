
import chessrepl.ChessRepl;
import utility.ServerFacade;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_BG_COLOR_MAGENTA;

public class ClientMain {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new ChessRepl(new ServerFacade(serverUrl), null, null, new NotificationHandler() {
        }, null).run("prelogin");
    }






}