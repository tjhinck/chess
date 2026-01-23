package chess;

import java.util.Collection;

public class BishopMovesCalc extends MovesCalc {
    int [][] legalDirections = {
            {1,1},  // up right
            {1,-1}, // up left
            {-1,1}, // down right
            {-1,-1} // down left
    };

    public BishopMovesCalc(ChessBoard board, ChessPosition position) {
        super(board, position);

    }

    public Collection<ChessMove> calculateBishopMoves(){
        return straightLineMoves(legalDirections);
    }
}
