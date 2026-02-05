package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn;
    ChessBoard board;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param teamColor the team whose turn it is
     */
    public void setTeamTurn(TeamColor teamColor) {
        teamTurn = teamColor;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece targetPiece = board.getPiece(startPosition);
        // check piece
        if (targetPiece == null){
            return null;
        }
        Collection<ChessMove> initialMoves = targetPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        // remove illegal moves
        for (ChessMove move : initialMoves) {
            try{
                ChessBoard boardCopy = board.clone();
                boardCopy.movePiece(move);
                validMoves.add(move);
            } catch (InvalidMoveException e){
                continue;
            }
        }
        return validMoves;
    }

    /**
     * returns true is the team has legal moves available to make
     * @param teamColor
     * @return
     */
    boolean hasLegalMoves(ChessGame.TeamColor teamColor){
        for (int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                ChessPiece piece = board.getPiece(row, col);
                if (piece != null && piece.getTeamColor() == teamColor){
                    Collection<ChessMove> legalMoves = validMoves(new ChessPosition(row, col));
                    if (legalMoves.size() > 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece targetPiece = board.getPiece(move.getStartPosition());
        if (targetPiece != null && targetPiece.getTeamColor() == teamTurn){
            Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
            if (validMoves.contains(move)){
                board.movePiece(move);
                teamTurn = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
                return;
            }
        }
        throw new InvalidMoveException();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return board.isInCheck(teamColor);
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (board.isInCheck(teamColor) && !hasLegalMoves(teamColor)){
            return true;
        } else{
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!board.isInCheck(teamColor) && !hasLegalMoves(teamColor)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param newBoard the new board to use
     */
    public void setBoard(ChessBoard newBoard) {
        board = newBoard;
        board.setKingLocations();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
