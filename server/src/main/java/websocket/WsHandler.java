package websocket;

import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import io.javalin.websocket.*;
import server.Server;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

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
    public void handleMessage(WsMessageContext ctx) {
        int gameId;
        Session session = ctx.session;

        try {
            UserGameCommand command = Server.GSON.fromJson(ctx.message(), UserGameCommand.class);
            gameId = command.getGameID();
            String username = authDao.getAuthData(command.getAuthToken()).username();
//            saveSession(gameId, session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, gameId);
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (Exception ex){

        }
        }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(Session session, String username, Integer gameID){
        connections.addSession(gameID, session);
        ServerMessage broadcastMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        broadcastMessage.setMessage();
    }
}
