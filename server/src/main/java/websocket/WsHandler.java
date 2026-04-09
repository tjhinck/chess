package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import response.ResponseException;
import server.Server;
import websocket.commands.*;
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
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command.getGameID());
                case LEAVE -> leaveGame(session, username, command.getGameID());
                case RESIGN -> resign(session, username, command.getGameID());
                case MAKE_MOVE -> {
                    MakeMoveCommand makeMoveCommand = Server.GSON.fromJson(ctx.message(), MakeMoveCommand.class);
                    makeMove(session, username,makeMoveCommand);
                }
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

    private void leaveGame(Session session, String username, Integer gameID) throws IOException, DataAccessException {
        connections.removeSession(gameID, session);
        GameData gameData = gameDao.getGame(gameID);
        if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
            GameData updatedGame = new GameData(gameData.gameID(), gameData.gameName(), gameData.chessGame(), null, gameData.blackUsername());
            gameDao.updateGame(updatedGame);
        } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)) {
            GameData updatedGame = new GameData(gameData.gameID(), gameData.gameName(), gameData.chessGame(), gameData.whiteUsername(), null);
            gameDao.updateGame(updatedGame);
        }
        ServerMessage broadcastMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        String message = username + " has left";
        broadcastMessage.setMessage(message);
        connections.broadcast(gameID, session, broadcastMessage);
    }

    private void makeMove(Session session, String username, MakeMoveCommand makeMoveCommand) throws DataAccessException, IOException {
        GameData gameData = gameDao.getGame(makeMoveCommand.getGameID());
        ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);
        if (playerColor == null){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error: unauthorized");
            session.getRemote().sendString(errorMessage.toString());
            return;
        }
        ChessGame game = gameData.chessGame();
        if (game.getTeamTurn() != playerColor){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error: Wait your turn");
            session.getRemote().sendString(errorMessage.toString());
            return;
        }
        try {
            game.makeMove(makeMoveCommand.getMove());
        } catch (InvalidMoveException e) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error: Illegal move");
            session.getRemote().sendString(errorMessage.toString());
            return;
        }

        ServerMessage moveNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        String notification = String.format("%s moved %s", username, makeMoveCommand.getMove().toCommandString());
        moveNotification.setMessage(notification);

        ServerMessage alert = null;
        if (game.isInStalemate(playerColor) || game.isInCheckmate(getOpponentColor(playerColor))) {
            game.endGame();
            alert = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            String gameOverMessage;
            if (game.isInStalemate(playerColor)) {
                gameOverMessage = "Game Over: Stalemate";
            } else {
                gameOverMessage = String.format("Game Over: Checkmate\n%s wins!", username);
            }
            alert.setMessage(gameOverMessage);
        } else if (game.isInCheck(getOpponentColor(playerColor))) {
            String checkMessage = String.format("%s is in check", getOpponentColor(playerColor).toString());
            alert.setMessage(checkMessage);
        }

        GameData updatedGame = new GameData(gameData.gameID(), gameData.gameName(), game, gameData.whiteUsername(), gameData.blackUsername());
        gameDao.updateGame(updatedGame);
        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadGameMessage.setChessGame(game);

        connections.broadcast(gameData.gameID(), session, moveNotification);
        connections.broadcast(gameData.gameID(), null, loadGameMessage);
        if (alert != null){
            connections.broadcast(gameData.gameID(), null, alert);
        }
    }

    private void resign(Session session, String username, Integer gameID) throws DataAccessException, IOException {
        GameData gameData = gameDao.getGame(gameID);
        ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);
        if (playerColor == null){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error: unauthorized");
            session.getRemote().sendString(errorMessage.toString());
            return;
        }
        ChessGame game = gameData.chessGame();
        try {
            game.assertGameInPlay();
        } catch (InvalidMoveException e) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error: game is already over");
            session.getRemote().sendString(errorMessage.toString());
            return;
        }
        game.endGame();
        GameData updatedGame = new GameData(gameData.gameID(), gameData.gameName(), game, gameData.whiteUsername(), gameData.blackUsername());
        gameDao.updateGame(updatedGame);
        ServerMessage moveNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        String notification = String.format("%s has resigned", username);
        moveNotification.setMessage(notification);

        connections.broadcast(gameData.gameID(), null, moveNotification);
    }

    private ChessGame.TeamColor getPlayerColor(GameData gameData, String username ) {
        if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)){
            return ChessGame.TeamColor.WHITE;
        } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)) {
            return ChessGame.TeamColor.BLACK;
        }
        return null;
    }

    private ChessGame.TeamColor getOpponentColor(ChessGame.TeamColor playerColor){
        return playerColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }
}
