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
    public GameClient(ServerFacade server, String auth, Integer gameID, NotificationHandler notificationHandler) {
        this.server = server;
        this.notif = notificationHandler;
        this.wb = new WebSocketFacade(server.getServerUrl(), notif);

        if (gameID != null) {this.gameID = gameID;}

        this.auth = auth;
        if (auth != null) {
        username = server.getUser(auth);}
        if (gameID != null && !observerCheck(server.getGame(gameID))) {
            GameData currentGame = server.getGame(gameID);
            wb.joinGame(auth,gameID,currentGame, username);
            System.out.print(printBoard());
        }
        else if (gameID != null && observerCheck(server.getGame(gameID))) {
            GameData currentGame = server.getGame(gameID);
            wb.observeGame(auth,gameID,currentGame, username);
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
        GameData currentGame = server.getGame(gameID);

        if (username.equals(currentGame.whiteUsername())) {
            currentGame = new GameData(currentGame.gameID(),null, currentGame.blackUsername(), currentGame.gameName(), currentGame.game());
            server.updateGame(currentGame);
        }
        else if (username.equals(currentGame.blackUsername())) {
            currentGame = new GameData(currentGame.gameID(), currentGame.whiteUsername(), null, currentGame.gameName(), currentGame.game());
            server.updateGame(currentGame);
        }
        wb.leaveGame(auth,gameID,username);
        return "GOODBYE\n";
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

            wb.makeMove(auth,gameID,updatedGameData, username);
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
        GameData currentGame = server.getGame(gameID);
        String winner;
        GameData updatedGame;
        if (username.equals(currentGame.whiteUsername())) {
            updatedGame = new GameData(currentGame.gameID(),"RESIGNED", currentGame.blackUsername(), currentGame.gameName(),currentGame.game());


            winner = currentGame.blackUsername();
        }
        else if (username.equals(currentGame.blackUsername())) {
            updatedGame = new GameData(currentGame.gameID(), currentGame.whiteUsername(), "RESIGNED", currentGame.gameName(),currentGame.game());

            winner = currentGame.whiteUsername();
        }
        else {
            return "CANNOT RESIGN";}

        server.updateGame(updatedGame);
        wb.resignGame(auth, gameID, username);
        return "Resigned from Game:";
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
