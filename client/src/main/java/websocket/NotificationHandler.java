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



public void loadGameNotifier(GameData gameData) {
    ChessGame currentGame = gameData.game();

    if (currentGame.getTeamTurn().equals(ChessGame.TeamColor.WHITE)){
        System.out.print("\n" + VisualizeBoard.produceWhiteBoard(currentGame,null));
        printPrompt();
    }

    else {
        System.out.print("\n" + VisualizeBoard.produceBlackBoard(currentGame,null));}

    if (currentGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
        System.out.print(gameData.blackUsername() + " WINS" );
    }
    else if (currentGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
        System.out.print(gameData.whiteUsername() + " WINS" );
    }
    else if (currentGame.isInStalemate(ChessGame.TeamColor.WHITE)) {
        System.out.print("THE GAME IS OVER: STALEMATE" );
    }
}



    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + ">>> ");
    }
}