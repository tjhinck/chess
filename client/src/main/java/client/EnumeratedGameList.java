package client;

import model.GameData;

import java.util.List;

public class EnumeratedGameList {
    private final List<GameData> games;

    public EnumeratedGameList(List<GameData> games){
        this.games = games;
    }

    public GameData get(int id){
        return games.get(id-1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < games.size(); i++){
            GameData game = games.get(i);
            sb.append(i+1);
            sb.append(". ");
            sb.append(game.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
