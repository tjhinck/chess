package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        List<ChessMove> moves = new ArrayList<>();
        // set values for white vs black pawn
        ChessGame.TeamColor pawnColor = piece.getTeamColor();
        int pawnDirection;
        int startRow;
        int promotionRow;
        if (pawnColor == ChessGame.TeamColor.WHITE){
            pawnDirection = 1;
            startRow = 2;
            promotionRow = 8;
        } else {
          pawnDirection = -1;
          startRow = 7;
          promotionRow = 1;
        }
        // check diagonal capture
        int newRow = position.getRow() + pawnDirection;
        int newCol = position.getColumn();
        int[] captureColumns = {newCol + 1, newCol - 1};
        for (int captureColumn : captureColumns){
            if (ChessPosition.isValid(newRow, captureColumn)){
                ChessPiece occupant = board.getPiece(newRow, captureColumn);
                if (occupant != null && occupant.getTeamColor() != pawnColor){
                    // check for promotion
                    if (newRow == promotionRow){
                        for (ChessPiece.PieceType pieceType : promotionTypes){
                            moves.add(new ChessMove(position, new ChessPosition(newRow, captureColumn), pieceType));
                        }
                    } else {
                        moves.add(new ChessMove(position, new ChessPosition(newRow, captureColumn), null));
                    }
                }
            }
        }
        // check moving one square in pawnDirection
        if (ChessPosition.isValid(newRow, newCol) && board.getPiece(newRow, newCol) == null){
            // check for promotion
            if (newRow == promotionRow){
                for (ChessPiece.PieceType pieceType : promotionTypes){
                    moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), pieceType));
                }
            } else{
                moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));
            }
            // moving two squares for first turn
            if (position.getRow() == startRow){
                newRow = position.getRow() + 2 * pawnDirection;
                if (board.getPiece(newRow, newCol) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));
                }
            }
        }


        return moves;
    }
}


