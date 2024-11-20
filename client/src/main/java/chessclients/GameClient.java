package chessclients;

import org.glassfish.tyrus.spi.WebSocketEngine;
import utility.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import javax.websocket.Endpoint;
import java.util.Arrays;

public class GameClient {
    private final ServerFacade server;
    private final WebSocketFacade wb;
    private NotificationHandler notif;
    private int gameID;
    private String auth;
    public GameClient(ServerFacade server, String auth, Integer gameID, NotificationHandler notificationHandler) {
        this.server = server;
        this.wb = new WebSocketFacade(server.getServerUrl(), notif);
        this.notif = notificationHandler;
        if (gameID != null) {this.gameID = gameID;}

        this.auth = auth;
        if (gameID != null) {
            wb.joinGame(auth,gameID);
        }
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
        wb.makeMove(auth,gameID);
       return "TEST";
    }
}
