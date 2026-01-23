package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalc extends MovesCalc {
    public BishopMovesCalc(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves(){
        List<ChessMove> moves = new ArrayList<>();
        // Check each diagonal and add valid moves to moves list
        // Up and right
        ChessPosition positionToCheck = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        while (positionToCheck.isValid()){
            moves.add(new ChessMove(position, positionToCheck, null));
            positionToCheck = new ChessPosition(positionToCheck.getRow()+1, positionToCheck.getColumn()+1);
        }
        // Down and right
        positionToCheck = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        while (positionToCheck.isValid()){
            moves.add(new ChessMove(position, positionToCheck, null));
            positionToCheck = new ChessPosition(positionToCheck.getRow()-1, positionToCheck.getColumn()+1);
        }
        // Up and left
        positionToCheck = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        while (positionToCheck.isValid()){
            moves.add(new ChessMove(position, positionToCheck, null));
            positionToCheck = new ChessPosition(positionToCheck.getRow()+1, positionToCheck.getColumn()-1);
        }
        // Down and left
        positionToCheck = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        while (positionToCheck.isValid()){
            moves.add(new ChessMove(position, positionToCheck, null));
            positionToCheck = new ChessPosition(positionToCheck.getRow()-1, positionToCheck.getColumn()-1);
        }

        return moves;

//        System.out.println("Finding bishop moves");
//        return List.of(new ChessMove(new ChessPosition(5,4), new ChessPosition(6,7), null));
    }
}
