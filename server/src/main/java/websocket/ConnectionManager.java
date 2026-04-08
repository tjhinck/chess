package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, Set<Session>> gameSessions = new ConcurrentHashMap<>();

    public void addSession(Integer gameID, Session userSession) {
        gameSessions.computeIfAbsent(gameID, key -> ConcurrentHashMap.newKeySet()).add(userSession);
    }

    public void removeSession(Integer gameID, Session session) {
        gameSessions.get(gameID).remove(session);
    }

    public void broadcast(Integer gameID, Session excludeSession, ServerMessage message) throws IOException {
        String msg = message.toString();
        for (Session c : gameSessions.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}