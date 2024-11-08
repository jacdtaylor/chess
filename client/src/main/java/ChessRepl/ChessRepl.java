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
private final ServerFacade server;
    public ChessRepl(ServerFacade server, String authToken) {
        auth = authToken;
        this.server = server;
        preLoginClient = new PreLoginClient(server);
        loginClient = new LoginClient(server, auth);
        gameClient = new GameClient(server);

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
                    new ChessRepl(server, result).run("postLogin");}

                else {System.out.print(result);}



                if (result.equals("game joined successfully")) {
                    new ChessRepl(server, auth).run("gameLogin");
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