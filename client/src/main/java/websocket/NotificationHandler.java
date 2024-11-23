package websocket;


import chess.ChessGame;
import model.GameData;
import ui.VisualizeBoard;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

public class NotificationHandler {
    public NotificationHandler() {}

    public void notify(ServerMessage message) {
        if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {


            GameData gameData = message.getGame();
            ChessGame currentGame = gameData.game();

            if (currentGame.getTeamTurn().equals(ChessGame.TeamColor.WHITE)){
            System.out.print("\n" + VisualizeBoard.produceWhiteBoard(currentGame,null));
            }

            else {
                System.out.print("\n" + VisualizeBoard.produceBlackBoard(currentGame,null));}
        }

        else {
        System.out.println("\n" + SET_TEXT_COLOR_MAGENTA + message.getMessage() + RESET_TEXT_COLOR);
        printPrompt();}
    };

    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + ">>> ");
    }
}