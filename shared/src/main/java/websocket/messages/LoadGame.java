package websocket.messages;

import model.GameData;

import java.util.Objects;

public class LoadGame extends ServerMessage{
    GameData game;
    String color;

    public LoadGame(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public GameData game() {
        return game;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
