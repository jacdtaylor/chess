package server;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
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

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {

        if (message.contains("move")) {
            MakeMove moveCommand = new Gson().fromJson(message, MakeMove.class);
            makeMove(moveCommand.getAuthToken(), moveCommand.getGameID(), moveCommand.getMove());

        }
        else {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case CONNECT -> connectUser(command.getAuthToken(), command.getGameID(), session);
            case LEAVE -> leaveUser(command.getAuthToken(), command.getGameID());
            case RESIGN -> resignUser(command.getAuthToken(), command.getGameID());
        }
        }
    }

    private void connectUser(String auth, int id, Session session) throws IOException {
        connections.add(auth,session,id);
        try {
            String user = Server.userService.getUser(auth);
            GameData realGame = Server.gameService.getGameData(id);

        String mess = user + " JOINED THE GAME";

        if (!user.equals(realGame.blackUsername()) && !user.equals(realGame.whiteUsername())) {
            mess += " AS AN OBSERVER";
        }
        Notification serverMess = new Notification(mess);

        connections.broadcast(auth, new Gson().toJson(serverMess), id, false);

        LoadGame loadGameNoti = new LoadGame(realGame);
        connections.broadcast(auth,new Gson().toJson(loadGameNoti),id, true); }

        catch (Exception e) {
            ErrorMessage error = new ErrorMessage("ERROR");
            connections.broadcast(auth, new Gson().toJson(error),id, true );
            connections.remove(auth);

        }

    }



    private void makeMove(String auth, int id, ChessMove move ) throws IOException {
        try {
            String user = Server.userService.getUser(auth);
            GameData newGame = Server.gameService.getGameData(id);
        LoadGame loadGameNoti = new LoadGame(newGame);
        connections.broadcast(auth,new Gson().toJson(loadGameNoti),id, false);
        String mess = user + " MADE A MOVE";
        Notification serverMess = new Notification(mess);

        connections.broadcast(auth, new Gson().toJson(serverMess), id, false);}
        catch (Exception e) {
            ErrorMessage error = new ErrorMessage("ERROR");
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
            ErrorMessage error = new ErrorMessage("ERROR");
            connections.broadcast(auth, new Gson().toJson(error),id, true );}
    }



    private void resignUser(String auth, int id) throws IOException {
        try{

            String user = Server.userService.getUser(auth);
            GameData game = Server.gameService.getGameData(id);
            ChessGame chessGame = game.game();
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
        chessGame.endGame();
        GameData resignedGameData = new GameData(game.gameID(),game.whiteUsername(),game.blackUsername()
        ,game.gameName(),chessGame);

        Server.gameService.updateGame(resignedGameData);

        Notification serverMess = new Notification(mess);
        connections.broadcast(null, new Gson().toJson(serverMess), id, false);}
        catch (Exception e) {
            ErrorMessage error = new ErrorMessage("ERROR");
            connections.broadcast(auth, new Gson().toJson(error),id, true );}
    }



}
