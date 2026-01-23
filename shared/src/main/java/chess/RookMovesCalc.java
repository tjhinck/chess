package chess;

import java.util.Collection;

public class RookMovesCalc extends MovesCalc{
    int [][] legalDirections = {
            {1,0},  // up
            {-1,0}, // down
            {0,1},  // right
            {0,-1}  // left
    };

    public RookMovesCalc(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> calculateRookMoves(){
        return straightLineMoves(legalDirections);
    }
}
