package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable{
    ChessPiece[][] squares = new ChessPiece[8][8];
    ChessPosition whiteKingPosition;
    ChessPosition blackKingPosition;

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

    public void movePiece(ChessMove move) throws InvalidMoveException{
        int[] start = {
                move.startPosition().getRow()-1,
                move.startPosition().getColumn()-1
        };
        int[] end = {
                move.endPosition().getRow()-1,
                move.endPosition().getColumn()-1
            };
        ChessPiece piece = getPiece(move.startPosition());
        ChessPiece.PieceType pieceType = piece.getPieceType();
        ChessGame.TeamColor movingTeam = piece.getTeamColor();

        squares[end[0]][end[1]] = squares[start[0]][start[1]];
        squares[start[0]][start[1]] = null;
        if (pieceType == ChessPiece.PieceType.KING){
            if (movingTeam == ChessGame.TeamColor.WHITE){
                whiteKingPosition = move.endPosition();
            } else {
                blackKingPosition = move.endPosition();
            }
        }
        // todo add pawn promotion
        if (isInCheck(movingTeam)){
            throw new InvalidMoveException();
        }
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
        whiteKingPosition = new ChessPosition(1,5);
        blackKingPosition = new ChessPosition(8, 5);
    }

    public void setKingLocations() {
        whiteKingPosition = findKing(ChessGame.TeamColor.WHITE);
        blackKingPosition = findKing(ChessGame.TeamColor.BLACK);
    }

    /**
     * Find king position to save for predetermined board
     * @param kingColor
     * @return position of that king
     */
    ChessPosition findKing(ChessGame.TeamColor kingColor){
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                ChessPiece piece = squares[row][col];
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == kingColor){
                    return new ChessPosition(row+1, col+1);
                }
            }
        }
        return null;
    }

    /**
     *
     * @param kingColor
     * @return true if that king is in check
     */
    public boolean isInCheck(ChessGame.TeamColor kingColor){
        ChessPosition kingPosition = kingColor == ChessGame.TeamColor.WHITE ? whiteKingPosition : blackKingPosition;
        // check all moves of the opposite color
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                ChessPiece piece = squares[row][col];
                if (piece != null && piece.getTeamColor() != kingColor){
                    Collection<ChessMove> moves = piece.pieceMoves(this, new ChessPosition(row+1, col+1));
                    for (ChessMove move : moves){
                        if (move.endPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            clone.squares = new ChessPiece[8][8];
            for (int row = 0; row < 8; row++){
                clone.squares[row] = Arrays.copyOf(squares[row], 8);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 8; row > 0; row--){ // go backwards to get right viewing order
            sb.append("|");
            for (int col = 1; col < 9; col++){
                ChessPiece piece = getPiece(row, col);
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
