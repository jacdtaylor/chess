package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesCalculator {



    public static Collection<ChessMove> BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> PossibleMoves = new ArrayList<ChessMove>();

        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        ChessPosition startPosition = new ChessPosition(row,col);
        int newrow = row;
        int newcol = col;
        while (newcol < 8 && newrow < 8) {
            newcol++;
            newrow++;
            ChessPosition endPosition = new ChessPosition(newrow,newcol);
            PossibleMoves.add(new ChessMove(startPosition,endPosition,null));


        }

    return PossibleMoves;
    }
}
