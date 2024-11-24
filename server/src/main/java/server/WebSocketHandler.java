package server;


import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.websocket.ConnectionManager;
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
        try {
            String user = Server.userService.getUser(auth);
            GameData realGame = Server.gameService.getGameData(id);



        String mess = user + " JOINED THE GAME";
        Notification serverMess = new Notification(mess);



        connections.broadcast(auth, new Gson().toJson(serverMess), id, false);
        LoadGame loadGameNoti = new LoadGame(realGame);

        connections.broadcast(auth,new Gson().toJson(loadGameNoti),id, true); } catch (Exception e) {
            ErrorMessage error = new ErrorMessage("ERROR");
            connections.broadcast(auth, new Gson().toJson(error),id, true );
            connections.remove(auth);

        }

    }

    private void connectObserver(String auth, int id, GameData game, Session session, String username) throws IOException {
        connections.add(auth,session,id);
        try {
            String user = Server.userService.getUser(auth);
            GameData realGame = Server.gameService.getGameData(id);



            String mess = user + " JOINED THE GAME AS AN OBSERVER";
            Notification serverMess = new Notification(mess);



            connections.broadcast(auth, new Gson().toJson(serverMess), id, false);
            LoadGame loadGameNoti = new LoadGame(realGame);

            connections.broadcast(auth,new Gson().toJson(loadGameNoti),id, true); } catch (Exception e) {
            ErrorMessage error = new ErrorMessage("ERROR");
            connections.broadcast(auth, new Gson().toJson(error),id, true );
            connections.remove(auth);

        }

    }


    private void makeMove(String auth, int id, GameData game, String username) throws IOException {
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

    private void leaveUser(String auth, int id, String username) throws IOException {
        try {
        String user = Server.userService.getUser(auth);
        connections.remove(auth);
        String mess = user + " LEFT THE GAME";
        Notification serverMess = new Notification(mess);


        connections.broadcast(auth, new Gson().toJson(serverMess), id, false);}
        catch (Exception e) {
            ErrorMessage error = new ErrorMessage("ERROR");
            connections.broadcast(auth, new Gson().toJson(error),id, true );}
    }

    private void resignUser(String auth, int id, String username) throws IOException {
        try{
            String user = Server.userService.getUser(auth);
        String mess = username + " RESIGNED";
        Notification serverMess = new Notification(mess);
        connections.broadcast(auth, new Gson().toJson(serverMess), id, false);}
        catch (Exception e) {
            ErrorMessage error = new ErrorMessage("ERROR");
            connections.broadcast(auth, new Gson().toJson(error),id, true );}
    }



}
