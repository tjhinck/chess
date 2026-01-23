package chess;

import java.util.Collection;

public class QueenMovesCalc extends MovesCalc{
    int [][] legalDirections = {
            {1,0},  // up
            {-1,0}, // down
            {0,1},  // right
            {0,-1},  // left
            // diagonals
            {1,1},  // up right
            {1,-1}, // up left
            {-1,1}, // down right
            {-1,-1} // down left
    };

    public QueenMovesCalc(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> calculateQueenMoves(){
        return straightLineMoves(legalDirections);
    }
}
