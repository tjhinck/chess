package model;

import chess.ChessGame;

public class GameData {
    private final int gameID;
    private String gameName;
    private final ChessGame chessGame;
    private String whiteUsername;
    private String blackUsername;

    public GameData(int gameID, String gameName, ChessGame chessGame) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.chessGame = chessGame;
    }

    public int getGameID(){
        return gameID;
    }

    public String getGameName(){
        return gameName;
    }

    public void setWhiteUsername(String username){
        whiteUsername = username;
    }

    public void setBlackUsername(String username){
        blackUsername = username;
    }

    public boolean isWhiteOpen(){
        return whiteUsername == null;
    }

    public boolean isBlackOpen(){
        return blackUsername == null;
    }
}
