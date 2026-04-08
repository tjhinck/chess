package websocket;

import websocket.messages.ServerMessage;

public interface WsMessageHandler {
    void notify(ServerMessage message);
}
