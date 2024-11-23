package server.websocket;


import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {

        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connectUser(command.getAuthToken(), command.getGameID(),command.getChessGame(), session, command.getUsername());
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), command.getChessGame(), command.getUsername());
            case LEAVE -> leaveUser(command.getAuthToken(), command.getGameID(), command.getUsername());
            case RESIGN -> resignUser(command.getAuthToken(), command.getGameID(), command.getUsername());
            case OBSERVE -> connectObserver(command.getAuthToken(),command.getGameID(),command.getChessGame(), session, command.getUsername());

        }
    }

    private void connectUser(String auth, int id, GameData game,Session session, String username) throws IOException {
        connections.add(auth,session,id);
        String mess = username + " JOINED THE GAME";
        Notification serverMess = new Notification(mess);
        connections.broadcast(auth, serverMess, id, false);
        LoadGame loadGameNoti = new LoadGame(game);

        connections.broadcast(auth,loadGameNoti,id, true);
    }

    private void connectObserver(String auth, int id, GameData game, Session session, String username) throws IOException {
        connections.add(auth,session,id);
        String mess = username + " JOINED THE GAME AS AN OBSERVER";
        Notification serverMess = new Notification(mess);
        connections.broadcast(auth, serverMess, id, false);

        LoadGame loadGameNoti = new LoadGame(game);
        connections.broadcast(auth,loadGameNoti,id, true);
    }


    private void makeMove(String auth, int id, GameData game, String username) throws IOException {
        LoadGame loadGameNoti = new LoadGame(game);
        connections.broadcast(auth,loadGameNoti,id, false);
        String mess = username + " MADE A MOVE";
        Notification serverMess = new Notification(mess);

        connections.broadcast(auth, serverMess, id, false);

    }

    private void leaveUser(String auth, int id, String username) throws IOException {
        connections.remove(auth);
        String mess = username + " LEFT THE GAME";
        Notification serverMess = new Notification(mess);


        connections.broadcast(auth, serverMess, id, false);
    }

    private void resignUser(String auth, int id, String username) throws IOException {
        String mess = username + " RESIGNED";
        Notification serverMess = new Notification(mess);


        connections.broadcast(auth, serverMess, id, false);
    }



}
