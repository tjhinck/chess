package chess;

import java.util.Collection;

public class KingMovesCalc extends MovesCalc{
    int [][] moveDirections = {
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

    public KingMovesCalc(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> calculateKingMoves() {
        return singleSquareMoves(moveDirections);
    }
}

