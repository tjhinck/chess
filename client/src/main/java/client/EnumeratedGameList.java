package client;

import model.GameDataDto;

import java.util.Collection;
import java.util.List;

public class EnumeratedGameList {
    private final List<GameDataDto> games;

    private EnumeratedGameList(Collection<GameDataDto> games){
        this.games = games.stream().toList();
    }

    public static EnumeratedGameList of(Collection<GameDataDto> listGamesResponse){
        return new EnumeratedGameList(listGamesResponse);
    }

    public GameDataDto get(int id){
        return games.get(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < games.size(); i++){
            GameDataDto game = games.get(i);
            sb.append(i+1);
            sb.append(". ");
            sb.append(game.gameName());
            sb.append(" White: ");
            sb.append(game.whiteUsername());
            sb.append( "Black: ");
            sb.append(game.blackUsername());
            sb.append("\n");
        }
        return sb.toString();
    }
}
