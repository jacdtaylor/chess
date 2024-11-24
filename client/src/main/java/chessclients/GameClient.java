package chessclients;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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
    public GameClient(ServerFacade server, String auth, Integer gameID, NotificationHandler notificationHandler, String color) {
        this.server = server;
        this.notif = notificationHandler;
        this.wb = new WebSocketFacade(server.getServerUrl(), notif);

        if (gameID != null) {this.gameID = gameID;}

        this.auth = auth;
        if (auth != null) {
        username = server.getUser(auth);}
        if (gameID != null && !observerCheck(server.getGame(gameID))) {
            wb.joinGame(auth,gameID, color);

        }
        else if (gameID != null && observerCheck(server.getGame(gameID))) {
            GameData currentGame = server.getGame(gameID);
            wb.observeGame(auth,gameID);


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
                case "quit" -> leaveGame();
                case "display" -> checkPiece(params);
                case "resign" -> resign();
                default -> "HELP";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }


    public String leaveGame() {
        wb.leaveGame(auth,gameID);
        return "GOODBYE\n";
    }

    public String takeAMove(String... params) throws Exception {
        ChessMove targetMove = MoveInterpreter.translateMove(params[0]);
        wb.makeMove(auth,gameID,targetMove);
        return "";
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


    public String checkPiece(String... params) {
        try {


            GameData currentGame = server.getGame(gameID);
            ChessGame game = currentGame.game();

            ChessPosition chessPosition = MoveInterpreter.translatePosition(params[0]);
            game.setTeamTurn(game.getBoard().getPiece(chessPosition).getTeamColor());

            currentGame = new GameData(currentGame.gameID(), currentGame.whiteUsername(), currentGame.blackUsername(), currentGame.gameName(), game);

            if (username.equals(currentGame.whiteUsername())) {
                return VisualizeBoard.produceWhiteBoard(game, chessPosition);
            } else if (username.equals(currentGame.blackUsername())) {
                return VisualizeBoard.produceBlackBoard(game, chessPosition);
            } else {
                return VisualizeBoard.produceWhiteBoard(game, chessPosition);
            }
        }
        catch (Exception ex) {
            return "PLEASE SELECT A PIECE";
        }
    }

    public String resign() {
        wb.resignGame(auth, gameID);
        return "";

    }

    private boolean observerCheck(GameData currentGame) {
        return (!Objects.equals(username, currentGame.whiteUsername()) && !Objects.equals(username, currentGame.blackUsername()));
    }


}
