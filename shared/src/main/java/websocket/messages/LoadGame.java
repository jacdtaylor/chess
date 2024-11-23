package websocket.messages;

import model.GameData;

public class LoadGame extends ServerMessage{

    GameData game;
    public LoadGame(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
    @Override
    public GameData getGame() {
        return game;
    }

    @Override
    public String getMessage() {
        return "";
    }

    @Override
    public String getError() {
        return "";
    }

    @Override
    public ServerMessageType getServerMessageType() {
        return ServerMessageType.LOAD_GAME;
    }
}
