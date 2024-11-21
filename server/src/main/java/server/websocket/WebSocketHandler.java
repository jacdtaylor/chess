package server.websocket;


import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {

        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connectUser(command.getAuthToken(), command.getGameID(), session, command.getUsername());
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), command.getChessGame(), command.getUsername());
            case LEAVE -> leaveUser(command.getAuthToken(), command.getGameID(), command.getUsername());
            case RESIGN -> resignUser(command.getAuthToken(), command.getGameID(), command.getUsername());

        }
    }

    private void connectUser(String auth, int id, Session session, String username) throws IOException {
        connections.add(auth,session,id);
        String mess = username + " JOINED THE GAME";
        ServerMessage serverMess = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);

        serverMess.addMessage(mess);
        connections.broadcast(auth, serverMess, id);
    }

    private void makeMove(String auth, int id, ChessGame game, String username) throws IOException {
        ServerMessage loadGameNoti = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadGameNoti.setChessGame(game);
        connections.broadcast(auth,loadGameNoti,id);
        String mess = username + " MADE A MOVE";
        ServerMessage serverMess = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        serverMess.addMessage(mess);

        connections.broadcast(auth, serverMess, id);

    }

    private void leaveUser(String auth, int id, String username) throws IOException {
        connections.remove(auth);
        String mess = username + " LEFT THE GAME";
        ServerMessage serverMess = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        serverMess.addMessage(mess);

        connections.broadcast(auth, serverMess, id);
    }

    private void resignUser(String auth, int id, String username) throws IOException {
        String mess = username + " RESIGNED";
        ServerMessage serverMess = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        serverMess.addMessage(mess);

        connections.broadcast(auth, serverMess, id);
    }



}
