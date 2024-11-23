package websocket.messages;

import model.GameData;

public class Notification extends ServerMessage{
    String message;
    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    @Override
    public GameData getGame() {
        return null;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getError() {
        return "";
    }

    @Override
    public ServerMessageType getServerMessageType() {
        return ServerMessageType.NOTIFICATION;
    }

}
