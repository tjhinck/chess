package websocket;

import chess.ChessGame;
import chess.GameRole;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import response.ResponseException;
import server.Server;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WsHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final AuthDao authDao;
    private final GameDao gameDao;
    private final ConnectionManager connections = new ConnectionManager();

    public WsHandler(AuthDao authDao, GameDao gameDao){
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }


    @Override
    public void handleMessage(WsMessageContext ctx) throws ResponseException {
        Session session = ctx.session;
        try {
            UserGameCommand command = Server.GSON.fromJson(ctx.message(), UserGameCommand.class);
            AuthData authData = authDao.getAuthData(command.getAuthToken());
            if (authData == null){
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                errorMessage.setErrorMessage("Error: unauthorized");
                session.getRemote().sendString(errorMessage.toString());
                return;
            }
            String username = authData.username();
//            gameId = command.getGameID();
//            saveSession(gameId, session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command.getGameID());
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (IOException | DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(Session session, String username, Integer gameID) throws DataAccessException, IOException, ResponseException {
        connections.addSession(gameID, session);
        GameData gameData = gameDao.getGame(gameID);
        if (gameData == null){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error: invalid game");
            session.getRemote().sendString(errorMessage.toString());
            return;
        }
        loadGame(session, gameData.chessGame());

        ServerMessage broadcastMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        String message;
        if (gameData.whiteUsername().equals(username)) {
            message = username + " has joined as white";
        } else if (gameData.blackUsername().equals(username)){
            message = username + " has joined as black";
        } else {
            message = username + " has joined as an observer";
        }
        broadcastMessage.setMessage(message);
        connections.broadcast(gameID, session, broadcastMessage);
    }

    private void loadGame(Session session, ChessGame game) throws IOException {
        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadGameMessage.setChessGame(game);
        session.getRemote().sendString(loadGameMessage.toString());
    }
}
