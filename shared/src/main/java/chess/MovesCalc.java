package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        }
        throw new RuntimeException("No matching piece type");

    }

    /**
     * Check for all valid moves in a straight line (horizontal, vertical, diagonal)
     *
     * @param legalDirections 2D array showing where to check for moves
     * @return list of valid moves
     */
    public Collection<ChessMove> straightLineMoves(int[][] legalDirections){
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
