//package client;

//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;

import java.util.Scanner;
import ui.*;

public class ChessRepl{

    PreLoginClient client;
    public ChessRepl(String serverUrl) {
        client = new PreLoginClient(serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
//        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("GOODBYE")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
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