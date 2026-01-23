package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Override
    public Collection<ChessMove> calculateMoves(){
        List<ChessMove> moves = new ArrayList<>();
        // check legal directions until reaching end of board or piece
        for (int[] direction : legalDirections){
            int newRow = position.getRow() + direction[0];
            int newCol = position.getColumn() + direction[1];

            while (ChessPosition.isValid(newRow, newCol)){
                ChessPiece occupant = board.getPiece(newRow, newCol);
                // empty square
                if (occupant == null){
                    moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));
                } else{
                    // piece occupying square
                    if (occupant.getTeamColor() != piece.getTeamColor()){
                        // capture allowed
                        moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));
                    }
                    // block further moves in this direction
                    break;
                }
                // continue in current direction
                newRow += direction[0];
                newCol += direction[1];
            }
        }
        return moves;
    }
}
