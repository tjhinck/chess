package websocket;

import chess.ChessGame.TeamColor;
import chess.ChessMove;
import chess.GameRole;
import com.google.gson.Gson;
import jakarta.websocket.*;
import response.ResponseException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WsFacade extends Endpoint {
    public static final Gson GSON = new Gson();
    Session session;
    WsMessageHandler messageHandler;

    public WsFacade(String url, WsMessageHandler messageHandler) throws ResponseException {
        try {
            this.messageHandler = messageHandler;

            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = GSON.fromJson(message, ServerMessage.class);
                    messageHandler.notify(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.HttpCode.serverError, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connect(String authToken, int gameID) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(GSON.toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.HttpCode.serverError, ex.getMessage());
        }
    }

    public void disconnect(String authToken, int gameID) throws ResponseException {
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(GSON.toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.HttpCode.serverError, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        try {
            var command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(GSON.toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.HttpCode.serverError, ex.getMessage());
        }
    }


}
