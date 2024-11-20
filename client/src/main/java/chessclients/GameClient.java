package chessclients;

import org.glassfish.tyrus.spi.WebSocketEngine;
import utility.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;

public class GameClient {
    private final ServerFacade server;
    private final WebSocketFacade wb;
    private NotificationHandler notif;
    private int gameID;
    public GameClient(ServerFacade server, String auth, Integer gameID, NotificationHandler notif) {
        this.server = server;
        this.wb = new WebSocketFacade(server.getServerUrl(), notif);
        this.notif = notif;
        this.gameID = gameID;
    }



    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "move" -> takeAMove(params);
                case "print" -> "print the board";
                case "quit" -> "GOODBYE\n";
                default -> "HELP";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }


    public String takeAMove(String... params) {
        wb.

    }
}
