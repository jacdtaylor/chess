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
import java.util.Objects;

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
        if (gameID != null && !observerCheck(server.getGame(gameID))) {
            wb.joinGame(auth,gameID, username);
            System.out.print(printBoard());
        }
        else if (gameID != null && observerCheck(server.getGame(gameID))) {
            wb.observeGame(auth,gameID,username);
            System.out.print(printBoard());


        }

    }



    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "move" -> takeAMove(params);
                case "print" -> printBoard();
                case "quit" -> "GOODBYE\n";
                default -> "HELP";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }


    public String takeAMove(String... params) throws Exception {
        GameData currentGame = server.getGame(gameID);

        ChessGame game = currentGame.game();

        ChessMove targetMove = MoveInterpreter.translateMove(params[0]);
        ChessGame.TeamColor currentColor = game.getTeamTurn();

        if (!Objects.equals(username, currentGame.whiteUsername()) && !Objects.equals(username, currentGame.blackUsername()))
        {return "OBSERVERS CANNOT MAKE MOVES\n";}
        if (username.equals(currentGame.whiteUsername()) && currentColor.equals(ChessGame.TeamColor.BLACK))
        {return "NOT YOUR TURN\n";}
        if (username.equals(currentGame.blackUsername()) && currentColor.equals(ChessGame.TeamColor.WHITE))
        {return "NOT YOUR TURN\n";}
        try {

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

    public String printBoard() {
        GameData currentGame = server.getGame(gameID);
        ChessGame game = currentGame.game();
      if (username.equals(currentGame.whiteUsername())) {
          return VisualizeBoard.produceWhiteBoard(game,null);}
      else if (username.equals(currentGame.blackUsername())) {
          return VisualizeBoard.produceBlackBoard(game, null);}
      else {
          return VisualizeBoard.produceWhiteBoard(game,null);
      }

    }


    private boolean observerCheck(GameData currentGame) {
        return (!Objects.equals(username, currentGame.whiteUsername()) && !Objects.equals(username, currentGame.blackUsername()));
    }


    private GameData getCurrentGame(int id)
        {Collection<GameData> allGames = server.listGames(auth);
        GameData[] gameArray = allGames.toArray(new GameData[0]);
        GameData currentGame = gameArray[id - 1];
        return currentGame;
        }
}
