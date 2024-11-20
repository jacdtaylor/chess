package websocket;


import websocket.messages.ServerMessage;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_BG_COLOR_MAGENTA;

public class NotificationHandler {
    public NotificationHandler() {}

    public void notify(ServerMessage message) {
        System.out.println("\n" + SET_BG_COLOR_MAGENTA + message.getMessage() + RESET_TEXT_COLOR);
        printPrompt();
    };

    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + "\n" + ">>> ");
    }
}