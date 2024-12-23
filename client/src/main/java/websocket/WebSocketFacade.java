package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exceptions.ResponseException;
import model.GameData;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_BG_COLOR_MAGENTA;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url,  NotificationHandler notificationHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;


            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);


            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {

                    if (message.contains("ERROR")) {
                            ErrorMessage serverMessage = new Gson().fromJson(message, ErrorMessage.class);
                            notificationHandler.errorNotifier(serverMessage.getErrorMessage());

                        } else if (message.contains("message")) {
                           Notification serverMessage = new Gson().fromJson(message, Notification.class);
                           notificationHandler.baseNotifier(serverMessage.getMessage());

                        } else {
                            LoadGame serverMessage = new Gson().fromJson(message, LoadGame.class);
                            GameData game = serverMessage.game();
                            if (serverMessage.getColor().equals("WHITE")) {
                            notificationHandler.loadGameNotifierWhite(game);}
                            else if (serverMessage.getColor().equals("BLACK")){
                                notificationHandler.loadGameNotifierBlack(game);
                            } else {
                                notificationHandler.loadGameNotifierWhite(game);
                            }
                        }




                }
            });

        }catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void joinGame(String auth, int gameID, String color) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, auth, gameID);
            action.setColor(color);

            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void observeGame(String auth, int gameID) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, auth, gameID);
            action.setColor("OBSERVER");
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }




    public void makeMove(String auth, int gameID, ChessMove move) throws ResponseException {
        try {
            var action = new MakeMove(auth, gameID, move);

            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void leaveGame(String auth, int gameID) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, auth, gameID);

            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }
    public void resignGame(String auth, int gameID) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, auth, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }




    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


}
