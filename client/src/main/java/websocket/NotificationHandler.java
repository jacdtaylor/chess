package websocket;


import chess.ChessGame;
import model.GameData;
import ui.VisualizeBoard;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

public class NotificationHandler {
    public NotificationHandler() {}


public void errorNotifier(String error) {
    System.out.println("\n" + SET_TEXT_COLOR_RED + error + RESET_TEXT_COLOR);
    printPrompt();}


public void baseNotifier(String notification) {
    System.out.println("\n" + SET_TEXT_COLOR_MAGENTA + notification + RESET_TEXT_COLOR);
    printPrompt();}


public void loadGameNotifierWhite(GameData gameData) {
    System.out.print("\n" + VisualizeBoard.produceWhiteBoard(gameData.game(),null));
    printPrompt();
}

public void loadGameNotifierBlack(GameData gameData) {
    System.out.print("\n" + VisualizeBoard.produceBlackBoard(gameData.game(),null));
    printPrompt();
}


    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + ">>> ");
    }
}