package websocket.messages;

import model.GameData;

public class Error extends ServerMessage{
String errorMessage;
    public Error(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    @Override
    public GameData getGame() {
        return null;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public String getError() {
        return errorMessage;
    }
    @Override
    public ServerMessageType getServerMessageType() {
        return ServerMessageType.ERROR;
    }

}
