package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesCalculator {





    public static void DiagonalRecursion(int row, int col, ChessBoard board, ChessPosition startPosition, String direction, ArrayList<ChessMove> PossibleMoves) {
        if (row == 0 || row == 9 || col == 0 || col == 9) {
            return;
        }

        ChessPosition endPosition = new ChessPosition(row,col);
        if (startPosition.getRow() != row || startPosition.getColumn() != col)
            PossibleMoves.add(new ChessMove(startPosition,endPosition,null));

        if (direction == "LD") {
            DiagonalRecursion(--row,--col,board,startPosition,"LD", PossibleMoves);
        }
        else if (direction == "RD") {
            DiagonalRecursion(--row,++col,board,startPosition,"RD", PossibleMoves);
        }
        else if (direction == "LU") {
            DiagonalRecursion(++row,--col,board,startPosition,"LU", PossibleMoves);
        }
        else {
            DiagonalRecursion(++row,++col,board,startPosition,"RU", PossibleMoves);
        }


    }

    public static Collection<ChessMove> BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> PossibleMoves = new ArrayList<ChessMove>();

        int col = myPosition.getColumn();
        int row = myPosition.getRow();

        DiagonalRecursion(row,col,board,myPosition,"LD",PossibleMoves);
        DiagonalRecursion(row,col,board,myPosition,"LU",PossibleMoves);
        DiagonalRecursion(row,col,board,myPosition,"RD",PossibleMoves);
        DiagonalRecursion(row,col,board,myPosition,"RU",PossibleMoves);


    return PossibleMoves;
    }
}
