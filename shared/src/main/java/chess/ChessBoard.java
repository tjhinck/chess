package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable{
    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    // overload for finding a piece without creating a ChessPosition object
    public ChessPiece getPiece (int row, int col){
        return squares[row-1][col-1];
    }

    public void movePiece(ChessMove move){
        int[] start = {
                move.getStartPosition().getRow()-1,
                move.getStartPosition().getColumn()-1
        };
        int[] end = {
                move.getEndPosition().getRow()-1,
                move.getEndPosition().getColumn()-1
            };
        squares[end[0]][end[1]] = squares[start[0]][start[1]];
        squares[start[0]][start[1]] = null;
        // todo add promotion
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[] backRank = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };
        // loop through columns and add pieces
        for (int col = 0; col < 8; col++){
            // add white pieces to back rank
            squares[0][col] = new ChessPiece(ChessGame.TeamColor.WHITE, backRank[col]);
            // add white pawns
            squares[1][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            // add black pawns
            squares[6][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            // add black pieces to back rank
            squares[7][col] = new ChessPiece(ChessGame.TeamColor.BLACK, backRank[col]);
        }
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            for (int row = 0; row < 8; row++){
                for (int col = 0; col < 8; col++){
                    clone.squares[row][col] = this.squares[row][col].clone();
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ChessPiece[] row : squares){
            sb.append("|");
            for (ChessPiece piece : row){
                if (piece == null){
                    sb.append(" ");
                } else{
                    sb.append(piece);
                }
                sb.append("|");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
