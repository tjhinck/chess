package chess;

import java.util.Collection;
import java.util.List;

public class MovesCalc {

    protected final ChessBoard board;
    protected final ChessPosition position;

    public MovesCalc(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> calculateMoves() {
        ChessPiece piece = board.getPiece(position);
        ChessPiece.PieceType pieceType = piece.getPieceType();

        switch (pieceType) {
            case KING -> {
                throw new RuntimeException("King not implemented");
            }
            case QUEEN -> {
                throw new RuntimeException("Queen Not implemented");
            }
            case BISHOP -> {
                BishopMovesCalc bishopMovesCalc = new BishopMovesCalc(board, position);
                return bishopMovesCalc.calculateMoves();
            }
            case KNIGHT -> {
                throw new RuntimeException("Knight Not implemented");
            }
            case ROOK -> {
                throw new RuntimeException("Rook Not implemented");
            }
            case PAWN -> {
                throw new RuntimeException("Pawn Not implemented");
            }
        }
        throw new RuntimeException("No matching piece type");
    }



}
