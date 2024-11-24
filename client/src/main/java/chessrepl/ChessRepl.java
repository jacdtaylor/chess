package chessrepl;//package client;

//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;

import chessclients.GameClient;
import chessclients.LoginClient;
import chessclients.PreLoginClient;
import utility.ServerFacade;
import utility.ValidUUID;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessRepl {
String auth;
String color;
PreLoginClient preLoginClient;
LoginClient loginClient;
GameClient gameClient;
Integer gameID;
NotificationHandler notif;
private final ServerFacade server;
    public ChessRepl(ServerFacade server, String authToken, Integer gameID, NotificationHandler notif, String color) {
        this.notif = notif;
        auth = authToken;
        this.gameID = gameID;
        this.server = server;
        preLoginClient = new PreLoginClient(server);
        loginClient = new LoginClient(server, auth);
        gameClient = new GameClient(server, auth, gameID, notif, color);


    }

    public void run(String clientVersion) {


        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("GOODBYE\n")) {
            switch (clientVersion) {
                case "postLogin" -> System.out.print("[AUTHORIZED]");
                case "gameLogin" -> System.out.print("[INGAME]");
                default ->          System.out.print("[UNAUTHORIZED]");
            }
            printPrompt();

            String line = scanner.nextLine();

            try {
                switch (clientVersion) {
                    case "postLogin" -> result = loginClient.eval(line);
                    case "gameLogin" -> result = gameClient.eval(line);
                    default -> result = preLoginClient.eval(line);
                }
                if (ValidUUID.isValidUUID(result)) {
                    new ChessRepl(server, result, gameID, notif, null).run("postLogin");}

                else if (result.contains("Join Game")) {
                    String numberString = result.replaceAll("[^0-9]", "");
                    int newID = Integer.parseInt(numberString);
                    new ChessRepl(server, auth, newID, notif, result.substring(result.length() - 5)).run("gameLogin");
                }
                else if (result.contains("Observe Game")) {
                    String numberString = result.replaceAll("[^0-9]", "");
                    int newID = Integer.parseInt(numberString);
                    new ChessRepl(server, auth, newID, notif, null).run("gameLogin");
                }

                else {System.out.print(SET_TEXT_COLOR_BLUE + result + RESET_TEXT_COLOR);}




            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }

    }
    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + "\n" + ">>> ");
    }


}