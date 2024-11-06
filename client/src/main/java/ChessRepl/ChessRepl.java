package ChessRepl;//package client;

//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;

import ChessClients.GameClient;
import ChessClients.LoginClient;
import ChessClients.PreLoginClient;
import utility.validUUID;

import java.util.Scanner;

public class ChessRepl {
String auth;
String serverUrl;
PreLoginClient preLoginClient;
LoginClient loginClient;
GameClient gameClient;
    public ChessRepl(String serverUrl, String authToken) {
        serverUrl = serverUrl;
        auth = authToken;
        preLoginClient = new PreLoginClient(serverUrl);
        loginClient = new LoginClient(serverUrl);
        gameClient = new GameClient(serverUrl);

    }

    public void run(String clientVersion) {
        switch (clientVersion) {
            case "postLogin" -> System.out.println("Enter Post Login Repl");
            case "gameLogin" -> System.out.println("Enter Game Repl");
            default ->          System.out.println("Welcome to Chess, Please Login or Register :D");
        }


//        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("GOODBYE")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                switch (clientVersion) {
                    case "postLogin" -> result = loginClient.eval(line);
                    case "gameLogin" -> result = gameClient.eval(line);
                    default -> result = preLoginClient.eval(line);
                }
                if (validUUID.isValidUUID(result)) {
                    new ChessRepl(serverUrl, result).run("postLogin");}

                else {System.out.print(result);}



                if (result.equals("game joined successfully")) {
                    new ChessRepl(serverUrl, auth).run("gameLogin");
                }

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