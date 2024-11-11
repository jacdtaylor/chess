package ChessRepl;//package client;

//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;

import ChessClients.GameClient;
import ChessClients.LoginClient;
import ChessClients.PreLoginClient;
import server.ServerFacade;
import utility.validUUID;

import java.util.Scanner;

public class ChessRepl {
String auth;
String serverUrl;
PreLoginClient preLoginClient;
LoginClient loginClient;
GameClient gameClient;
Integer gameID;
private final ServerFacade server;
    public ChessRepl(ServerFacade server, String authToken, Integer gameID) {
        auth = authToken;
        this.gameID = gameID;
        this.server = server;
        preLoginClient = new PreLoginClient(server);
        loginClient = new LoginClient(server, auth);
        gameClient = new GameClient(server, auth, gameID);

    }

    public void run(String clientVersion) {



//        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("GOODBYE")) {
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
                if (validUUID.isValidUUID(result)) {
                    new ChessRepl(server, result, gameID).run("postLogin");}


                else if (result.contains("Join Game")) {
                    String numberString = result.replaceAll("[^0-9]", "");
                    int newID = Integer.parseInt(numberString);
                    new ChessRepl(server, auth, newID).run("gameLogin");
                }


                else {System.out.print(result);}




            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

//    public void notify(Notification notification) {
//        System.out.println(notification.message());
//        printPrompt();
//    }

    private void printPrompt() {
        System.out.print("\n" + ">>> " );
    }

}