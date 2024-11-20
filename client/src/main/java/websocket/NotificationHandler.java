package websocket;


import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

public class NotificationHandler {
    public NotificationHandler() {}

    public void notify(ServerMessage message) {
        if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {


        }


        System.out.println("\n" + SET_TEXT_COLOR_MAGENTA + message.getMessage() + RESET_TEXT_COLOR);
        printPrompt();
    };

    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + "\n" + ">>> ");
    }
}