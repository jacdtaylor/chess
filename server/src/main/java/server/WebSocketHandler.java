package server;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import exception.DataAccessException;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.websocket.ConnectionManager;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import javax.xml.crypto.Data;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {

        if (message.contains("move")) {
            MakeMove moveCommand = new Gson().fromJson(message, MakeMove.class);
            makeMove(moveCommand.getAuthToken(), moveCommand.getGameID(), moveCommand.getMove(), session);

        }
        else {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case CONNECT -> connectUser(command.getAuthToken(), command.getGameID(), session, command.getColor());
            case LEAVE -> leaveUser(command.getAuthToken(), command.getGameID());
            case RESIGN -> resignUser(command.getAuthToken(), command.getGameID());
        }
        }
    }

    private void connectUser(String auth, int id, Session session, String color) throws IOException {
        connections.add(auth,session,id);
        try {
            String user = Server.userService.getUser(auth);
            GameData realGame = Server.gameService.getGameData(id);

        String mess = user + " JOINED THE GAME AS " + color;


        Notification serverMess = new Notification(mess);

        connections.broadcast(auth, new Gson().toJson(serverMess), id, false);

        LoadGame loadGameNoti = new LoadGame(realGame);
        loadGameNoti.setColor(color.toUpperCase());
        connections.broadcast(auth,new Gson().toJson(loadGameNoti),id, true); }

        catch (Exception e) {
            ErrorMessage error = new ErrorMessage("ERROR");
            connections.broadcast(auth, new Gson().toJson(error),id, true );
            connections.remove(auth);

        }

    }



    private void makeMove(String auth, int id, ChessMove move, Session session) throws IOException {
        try {
            String selfCol = "BLACK";
            String otherCol = "WHITE";
            String user = Server.userService.getUser(auth);
            GameData newGame = Server.gameService.getGameData(id);
            if (newGame.game().getTeamTurn().equals(ChessGame.TeamColor.WHITE)) {
                selfCol = "WHITE";
                otherCol = "BLACK";
            }
            if (newGame.game().isGameOver()) {
                ErrorMessage errorResign = new ErrorMessage("ERROR: GAME IS ALREADY OVER");
                connections.broadcast(auth, new Gson().toJson(errorResign),id, true );
                return;
            }

            if ((newGame.game().getTeamTurn().equals(ChessGame.TeamColor.WHITE) &&
                    !user.equals(newGame.whiteUsername()))
            | (newGame.game().getTeamTurn().equals(ChessGame.TeamColor.BLACK) &&
                    !user.equals(newGame.blackUsername()))) {
                ErrorMessage errorResign = new ErrorMessage("ERROR: NOT YOUR TURN");
                connections.broadcast(auth, new Gson().toJson(errorResign),id, true );
                return;
            }

            if (!user.equals(newGame.blackUsername()) && !user.equals(newGame.whiteUsername())) {
                ErrorMessage errorResign = new ErrorMessage("ERROR: OBSERVERS CANNOT PLAY");
                connections.broadcast(auth, new Gson().toJson(errorResign),id, true );
                return;
            }



            newGame.game().makeMove(move);
            Server.gameService.updateGame(newGame);

        LoadGame loadGameNoti = new LoadGame(newGame);
        loadGameNoti.setColor(otherCol);
        connections.broadcast(auth,new Gson().toJson(loadGameNoti),id, false);

        LoadGame loadGameSelf = new LoadGame(newGame);
        loadGameSelf.setColor(selfCol);
        connections.broadcast(auth,new Gson().toJson(loadGameSelf),id, true);


        String mess = user + " MADE A MOVE";
        Notification serverMess = new Notification(mess);
        connections.broadcast(auth, new Gson().toJson(serverMess), id, false);


            if (newGame.game().isInCheckmate(ChessGame.TeamColor.WHITE))
            {
                String check = newGame.blackUsername() + " WINS!!!!";
                Notification checkNoti = new Notification(check);
                connections.broadcast(null, new Gson().toJson(checkNoti), id, false);
                newGame.game().endGame();
                Server.gameService.updateGame(newGame);
            }
            else if (newGame.game().isInCheckmate(ChessGame.TeamColor.BLACK))
            {
                String check = newGame.whiteUsername() + " WINS!!!!";
                Notification checkNoti = new Notification(check);
                connections.broadcast(null, new Gson().toJson(checkNoti), id, false);
                newGame.game().endGame();
                Server.gameService.updateGame(newGame);
            }
            else if (newGame.game().isInStalemate(ChessGame.TeamColor.BLACK) | newGame.game().isInStalemate(ChessGame.TeamColor.WHITE) )
            {
                String check = "GAME IS OVER: STALEMATE";
                Notification checkNoti = new Notification(check);
                connections.broadcast(null, new Gson().toJson(checkNoti), id, false);
                newGame.game().endGame();
                Server.gameService.updateGame(newGame);
            }
            else if (newGame.game().isInCheck(ChessGame.TeamColor.WHITE))
            {
                String check = newGame.whiteUsername() + " IS IN CHECK";
                Notification checkNoti = new Notification(check);
                connections.broadcast(null, new Gson().toJson(checkNoti), id, false);
            }
            else if (newGame.game().isInCheck(ChessGame.TeamColor.BLACK))
            {
                String check = newGame.blackUsername() + " IS IN CHECK";
                Notification checkNoti = new Notification(check);
                connections.broadcast(null, new Gson().toJson(checkNoti), id, false);
            }



        }
        catch (DataAccessException ex) {
            connections.add(auth,session,id);
            ErrorMessage error = new ErrorMessage("ERROR: " + ex.getMessage());
            connections.broadcast(auth, new Gson().toJson(error),id, true );
        connections.remove(auth);
        } catch (Exception e) {
            ErrorMessage error = new ErrorMessage("ERROR: INVALID MOVE");
            connections.broadcast(auth, new Gson().toJson(error),id, true );}
    }

    private void leaveUser(String auth, int id) throws IOException {
        try {
        String user = Server.userService.getUser(auth);
        GameData game = Server.gameService.getGameData(id);

        if (user.equals(game.whiteUsername())) {
            GameData whiteLeft = new GameData(game.gameID(),null,game.blackUsername(),
                    game.gameName(), game.game());
            Server.gameService.updateGame(whiteLeft);
        }
        if (user.equals(game.blackUsername())) {
            GameData blackLeft = new GameData(game.gameID(),game.whiteUsername(),null,
                    game.gameName(), game.game());
            Server.gameService.updateGame(blackLeft);
        }

        connections.remove(auth);
        String mess = user + " LEFT THE GAME";
        Notification serverMess = new Notification(mess);


        connections.broadcast(auth, new Gson().toJson(serverMess), id, false);}
        catch (Exception e) {

            ErrorMessage error = new ErrorMessage("ERROR: " +e.getMessage() );
            connections.broadcast(auth, new Gson().toJson(error),id, true );}
    }



    private void resignUser(String auth, int id) throws IOException {
        try{

            String user = Server.userService.getUser(auth);
            GameData game = Server.gameService.getGameData(id);

        if (!user.equals(game.blackUsername()) && !user.equals(game.whiteUsername())) {
            ErrorMessage error = new ErrorMessage("ERROR: OBSERVERS CANNOT RESIGN");
            connections.broadcast(auth, new Gson().toJson(error),id, true );
            return;
            }
        if (game.game().isGameOver()) {
            ErrorMessage errorResign = new ErrorMessage("ERROR: GAME IS ALREADY OVER");
            connections.broadcast(auth, new Gson().toJson(errorResign),id, true );
            return;
        }

        String mess = user + " RESIGNED";

        game.game().endGame();
        Server.gameService.updateGame(game);

        Notification serverMess = new Notification(mess);
        connections.broadcast(null, new Gson().toJson(serverMess), id, false);}
        catch (Exception e) {
            ErrorMessage error = new ErrorMessage("ERROR");
            connections.broadcast(auth, new Gson().toJson(error),id, true );}
    }



}
