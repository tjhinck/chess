package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalc extends MovesCalc{
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

    public KingMovesCalc(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> calculateKingMoves() {
        List<ChessMove> moves = new ArrayList<>();
        // check each direction once
        for (int[] direction : legalDirections) {
            int newRow = position.getRow() + direction[0];
            int newCol = position.getColumn() + direction[1];
            if (ChessPosition.isValid(newRow, newCol)) {
                ChessPiece occupant = board.getPiece(newRow, newCol);
                // valid if not occupied or occupied by opposite color
                if (occupant == null || occupant.getTeamColor() != piece.getTeamColor()) {
                    moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));
                }
            }
        }
        return moves;
    }
}

