package server.websocket;


import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {

        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connectUser(command.getAuthToken(), command.getGameID(), session);
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID());
            case LEAVE -> leaveUser(command.getAuthToken(), command.getGameID());
            case RESIGN -> resignUser(command.getAuthToken(), command.getGameID());

        }
    }

    private void connectUser(String auth, int id, Session session) {

    }

    private void makeMove(String auth, int id) {}

    private void leaveUser(String auth, int id) {}

    private void resignUser(String auth, int id) {}



}
