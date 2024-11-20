package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    public int currentID;

    public Connection(String visitorName, Session session, int currentID) {
        this.visitorName = visitorName;
        this.session = session;
        this.currentID = currentID;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}