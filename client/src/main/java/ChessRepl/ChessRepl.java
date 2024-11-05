package ChessRepl;//package client;

//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;

import ChessClients.GameClient;
import ChessClients.LoginClient;
import ChessClients.PreLoginClient;

import java.util.Scanner;

public class ChessRepl {
String serverUrl;
PreLoginClient preLoginClient;
LoginClient loginClient;
GameClient gameClient;
    public ChessRepl(String serverUrl) {
        serverUrl = serverUrl;
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

                System.out.print(result);

                if (result.equals("welcome")) {
                    new ChessRepl(serverUrl).run("postLogin");
                }
                if (result.equals("game joined successfully")) {
                    new ChessRepl(serverUrl).run("gameLogin");
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