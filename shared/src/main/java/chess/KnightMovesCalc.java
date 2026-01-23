package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalc extends MovesCalc {
    int[][] legalJumps = {
        {-2, -1}, {-2,  1},
        {-1, -2}, {-1,  2},
        { 1, -2}, { 1,  2},
        { 2, -1}, { 2,  1}
    };

    public KnightMovesCalc(ChessBoard board, ChessPosition position) {
        super(board, position);
    }
    public Collection<ChessMove> calculateKingMoves() {
        List<ChessMove> moves = new ArrayList<>();
        // check if each knight jump is allowed
        for (int[] jump : legalJumps){
            int newRow = position.getRow() + jump[0];
            int newCol = position.getColumn() + jump[1];
            if (ChessPosition.isValid(newRow, newCol)){
                ChessPiece occupant = board.getPiece(newRow, newCol);
                if (occupant == null || occupant.getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));
                }
            }
        }
        return moves;
    }
}
