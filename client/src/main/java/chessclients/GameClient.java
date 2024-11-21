package chessclients;

import chess.ChessGame;
import chess.ChessMove;
import model.GameData;
import org.glassfish.tyrus.spi.WebSocketEngine;
import ui.VisualizeBoard;
import utility.MoveInterpreter;
import utility.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import javax.websocket.Endpoint;
import java.util.Arrays;
import java.util.Collection;

public class GameClient {
    private final ServerFacade server;
    private final WebSocketFacade wb;
    private final NotificationHandler notif;
    private int gameID;
    private final String auth;
    String username;
    public GameClient(ServerFacade server, String auth, Integer gameID, NotificationHandler notificationHandler) {
        this.server = server;
        this.notif = notificationHandler;
        this.wb = new WebSocketFacade(server.getServerUrl(), notif);

        if (gameID != null) {this.gameID = gameID;}

        this.auth = auth;
        if (auth != null) {
        username = server.getUser(auth);}
        if (gameID != null) {
            wb.joinGame(auth,gameID, username);
        }
    }



    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "move" -> takeAMove(params);
                case "print" -> "print the board";
                case "quit" -> "GOODBYE\n";
                default -> "HELP";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }


    public String takeAMove(String... params) throws Exception {
        GameData currentGame = server.getGame(gameID);
        int newID = currentGame.gameID();
        ChessGame game = currentGame.game();
        ChessMove targetMove = MoveInterpreter.translateMove(params[0]);
        try {
            ChessGame.TeamColor currentColor = game.getTeamTurn();
            game.makeMove(targetMove);
            GameData updatedGameData = new GameData(currentGame.gameID(), currentGame.whiteUsername(),
                    currentGame.blackUsername(), currentGame.gameName(), game);
            server.updateGame(updatedGameData);

            wb.makeMove(auth,gameID,game, username);
            if (currentColor.equals(ChessGame.TeamColor.WHITE)) {return VisualizeBoard.produceWhiteBoard(game, null);}
            else {return VisualizeBoard.produceBlackBoard(game,null);}

        }
        catch (Exception ex) {
            return "INVALID MOVE";
        }




    }

    private GameData getCurrentGame(int id)
        {Collection<GameData> allGames = server.listGames(auth);
        GameData[] gameArray = allGames.toArray(new GameData[0]);
        GameData currentGame = gameArray[id - 1];
        return currentGame;
        }
}
