package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalc extends MovesCalc{
    ChessPiece.PieceType[] promotionTypes = {
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.QUEEN
    };

    public PawnMovesCalc(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> calculatePawnMoves(){
        Collection<ChessMove> moves = new ArrayList<>();
        int pawnDirection;
        int firstMoveRow;
        int promotionRow;
        // set values specific to pawn color
        ChessGame.TeamColor pawnColor = piece.getTeamColor();
        if (pawnColor == ChessGame.TeamColor.WHITE){
            pawnDirection = 1;
            firstMoveRow = 3;
            promotionRow = 8;
        } else {
          pawnDirection = -1;
          firstMoveRow = 6;
          promotionRow = 1;
        }
        int newRow = position.getRow() + pawnDirection;
        int newCol = position.getColumn();
        // check diagonal capture
        int[] captureColumns = {newCol + 1, newCol - 1};
        for (int captureCol : captureColumns){
            if (ChessPosition.isValid(newRow, captureCol)){
                ChessPiece occupant = board.getPiece(newRow, captureCol);
                if (occupant != null && occupant.getTeamColor() != pawnColor){
                    if (newRow == promotionRow){
                        // add move for each promotion type
                        for (ChessPiece.PieceType pieceType : promotionTypes){
                            moves.add(new ChessMove(position, new ChessPosition(newRow, captureCol), pieceType));
                        }
                    } else {
                        moves.add(new ChessMove(position, new ChessPosition(newRow, captureCol), null));
                    }
                }
            }
        }
        // check moving forward one square
        if (ChessPosition.isValid(newRow, newCol)){
            ChessPiece occupant = board.getPiece(newRow, newCol);
            if (occupant == null){
                if (newRow == promotionRow){
                    // add move for each promotion
                    for (ChessPiece.PieceType pieceType : promotionTypes){
                        moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), pieceType));
                    }
                } else {
                    moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));
                }
                // check for first move double squares
                if (newRow == firstMoveRow){
                    newRow += pawnDirection;
                    occupant = board.getPiece(newRow, newCol);
                    if (occupant == null){
                        moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));
                    }
                }
            }
        }

        return moves;
    }
}


