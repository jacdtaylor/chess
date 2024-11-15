package utility;

import chess.ChessMove;
import chess.ChessPosition;

public class MoveInterpreter {




    public static ChessPosition translatePosition(String code) throws Exception {
        code = code.toUpperCase();
        int x = 0;
        int y = Integer.parseInt(String.valueOf(code.charAt(1)));

        switch (code.charAt(0)) {
            case 'A' -> {x = 1;}
            case 'B' -> {x = 2;}
            case 'C' -> {x = 3;}
            case 'D' -> {x = 4;}
            case 'E' -> {x = 5;}
            case 'F' -> {x = 6;}
            case 'G' -> {x = 7;}
            case 'H' -> {x = 8;}
            default -> throw new Exception("Invalid Move");
        }
return new ChessPosition(y,x);
    }



    public static ChessMove translateMove(String moveCode) throws Exception {
        String firstPos = moveCode.substring(0,2);
        String lastPos = moveCode.substring(2,4);
        ChessPosition startPos = translatePosition(firstPos);
        ChessPosition endPos = translatePosition(lastPos);

        return new ChessMove(startPos,endPos,null);
    }
}
