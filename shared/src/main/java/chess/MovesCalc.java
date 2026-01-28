package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesCalc {

    protected final ChessBoard board;
    protected final ChessPosition position;
    protected final ChessPiece piece;

    public MovesCalc(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.piece = board.getPiece(position);
    }

    public Collection<ChessMove> calculateMoves() {
        ChessPiece.PieceType pieceType = piece.getPieceType();

        switch (pieceType) {
            case KING -> {
                KingMovesCalc kingMovesCalc = new KingMovesCalc(board,position);
                return kingMovesCalc.calculateKingMoves();
            }
            case QUEEN -> {
                QueenMovesCalc queenMovesCalc = new QueenMovesCalc(board, position);
                return queenMovesCalc.calculateQueenMoves();
            }
            case BISHOP -> {
                BishopMovesCalc bishopMovesCalc = new BishopMovesCalc(board, position);
                return bishopMovesCalc.calculateBishopMoves();
            }
            case KNIGHT -> {
                KnightMovesCalc knightMovesCalc = new KnightMovesCalc(board, position);
                return knightMovesCalc.calculateKingMoves();
            }
            case ROOK -> {
                RookMovesCalc rookMovesCalc = new RookMovesCalc(board, position);
                return rookMovesCalc.calculateRookMoves();
            }
            case PAWN -> {
                PawnMovesCalc pawnMovesCalc = new PawnMovesCalc(board, position);
                return pawnMovesCalc.calculatePawnMoves();
            }
            default -> throw new RuntimeException("Invalid Piece Type");
        }
    }

    /**
     * Check for all valid moves in a straight line (horizontal, vertical, diagonal)
     *
     * @param moveDirections array of arrays showing where to check for moves
     * @return list of valid moves
     */
    public Collection<ChessMove> straightLineMoves(int[][] moveDirections){
        Collection<ChessMove> moves = new ArrayList<>();
        int currentRow = position.getRow();
        int currentCol = position.getColumn();
        // check legal directions until reaching a piece or end of board
        for (int[] direction : moveDirections){
            int newRow = currentRow + direction[0];
            int newCol =  currentCol + direction[1];
            // continue until blocked
            while (ChessPosition.isValid(newRow, newCol)){
                ChessPiece occupant = board.getPiece(newRow, newCol);
                if (occupant == null){
                    // empty square
                    moves.add(new ChessMove(position, new ChessPosition(newRow, newCol), null));
                } else{
                    if (occupant.getTeamColor() != piece.getTeamColor()){
                        // allow capture
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

    /**
     * Check one valid move for each move direction
     * Works for any single move
     * @param moveDirections array of arrays showing where to check for moves
     * @return list of valid moves
     */
    public Collection<ChessMove> singleSquareMoves(int[][] moveDirections){
        Collection<ChessMove> moves = new ArrayList<>();
        int currentRow = position.getRow();
        int currentCol = position.getColumn();
        // check each direction once
        for (int[] direction : moveDirections){
            int newRow = currentRow + direction[0];
            int newCol = currentCol + direction[1];
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
