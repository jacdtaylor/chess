package chess;

import java.util.ArrayList;

public class MoveCalculator {

    public static boolean checkMove(int row, int col, ChessBoard board, ChessGame.TeamColor myColor) {
        ChessPosition endPosition = new ChessPosition(row,col);

        if (row <=0 || col <= 0 || row >= 9 || col >= 9) {return true;}

        return board.getPiece(endPosition) != null &&
                board.getPiece(endPosition).getTeamColor().equals(myColor);
    }


    public static void diagonalRecursion(int row, int col, ChessBoard board, ChessPosition myPosition,
                                         ArrayList<ChessMove> possibleMoves, String direction) {

        ChessPosition endPosition = new ChessPosition(row,col);
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        if (checkMove(row, col, board, myColor)) {return;}

        possibleMoves.add(new ChessMove(myPosition,endPosition,null));
        if (board.getPiece(endPosition) == null && board.getPiece(myPosition).getPieceType() != ChessPiece.PieceType.KING) {
            switch (direction) {
                case "UR":
                    diagonalRecursion(++row,++col,board,myPosition, possibleMoves,direction);
                    return;
                case "DR":
                    diagonalRecursion(--row,++col,board,myPosition, possibleMoves,direction);
                    return;
                case "DL":
                    diagonalRecursion(--row,--col,board,myPosition, possibleMoves,direction);
                    return;
                case "UL":
                    diagonalRecursion(++row,--col,board,myPosition, possibleMoves,direction);
                    return;
            }
        }
    }

    public static void straightRecursion(int row, int col, ChessBoard board, ChessPosition myPosition,
                                         ArrayList<ChessMove> possibleMoves, String direction) {
        ChessPosition endPosition = new ChessPosition(row,col);
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        if (checkMove(row, col, board, myColor)) {return;}


        possibleMoves.add(new ChessMove(myPosition,endPosition,null));
        if (board.getPiece(endPosition) == null && board.getPiece(myPosition).getPieceType() != ChessPiece.PieceType.KING) {

            switch (direction) {
                case "R":
                    straightRecursion(row,++col,board,myPosition, possibleMoves,direction);
                    return;
                case "D":
                    straightRecursion(--row,col,board,myPosition, possibleMoves,direction);
                    return;
                case "L":
                    straightRecursion(row,--col,board,myPosition, possibleMoves,direction);
                    return;
                case "U":
                    straightRecursion(++row,col,board,myPosition, possibleMoves,direction);

            }
        }
    }

    public static void knightHelper(int row, int col, ChessBoard board, ChessPosition myPosition,
                                         ArrayList<ChessMove> possibleMoves) {
        ChessPosition endPosition = new ChessPosition(row,col);
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        if (checkMove(row, col, board, myColor)) {return;}
        possibleMoves.add(new ChessMove(myPosition,endPosition,null));
    }

    public static void pawnHelperTake(int row, int col, ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> possibleMoves,
                                         ArrayList<ChessPiece.PieceType> promotion, ChessGame.TeamColor myColor) {
        if (row <=0 || col <= 0 || row >= 9 || col >= 9) {return;}
        ChessPosition endPosition = new ChessPosition(row,col);
        if (board.getPiece(endPosition) == null ||
                board.getPiece(endPosition).getTeamColor().equals(myColor))
        {return;}
        for (ChessPiece.PieceType pro : promotion) {
        possibleMoves.add(new ChessMove(myPosition,endPosition,pro));}
    }

    public static void pawnHelper(int row, int col, ChessBoard board, ChessPosition myPosition,
                                         ArrayList<ChessMove> possibleMoves, ArrayList<ChessPiece.PieceType> promotion, ChessGame.TeamColor myColor) {
        if (row <=0 || col <= 0 || row >= 9 || col >= 9) {return;}
        ChessPosition endPosition = new ChessPosition(row,col);
        if (board.getPiece(endPosition) != null)
        {return;}
        for (ChessPiece.PieceType pro : promotion) {
            possibleMoves.add(new ChessMove(myPosition,endPosition,pro));}
        if (myColor == ChessGame.TeamColor.WHITE && row == 3) {
            pawnHelper(4,col,board,myPosition,possibleMoves,promotion,myColor);
        }
        if (myColor == ChessGame.TeamColor.BLACK && row == 6) {
            pawnHelper(5,col,board,myPosition,possibleMoves,promotion,myColor);
        }}

    public static void bishopMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> possibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        diagonalRecursion(row + 1, col + 1, board, myPosition,possibleMoves, "UR");
        diagonalRecursion(row - 1, col - 1, board, myPosition,possibleMoves, "DL");
        diagonalRecursion(row + 1, col - 1, board, myPosition,possibleMoves, "UL");
        diagonalRecursion(row - 1, col + 1, board, myPosition,possibleMoves, "DR");

    }


    public static void rookMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> possibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        straightRecursion(row,col + 1,board,myPosition,possibleMoves,"R");
        straightRecursion(row,col - 1,board,myPosition,possibleMoves,"L");
        straightRecursion(row - 1,col,board,myPosition,possibleMoves,"D");
        straightRecursion(row + 1,col,board,myPosition,possibleMoves,"U");
    }


    public static void royaltyMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> possibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        diagonalRecursion(row + 1, col + 1, board, myPosition,possibleMoves, "UR");
        diagonalRecursion(row - 1, col - 1, board, myPosition,possibleMoves, "DL");
        diagonalRecursion(row + 1, col - 1, board, myPosition,possibleMoves, "UL");
        diagonalRecursion(row - 1, col + 1, board, myPosition,possibleMoves, "DR");
        straightRecursion(row,col + 1,board,myPosition,possibleMoves,"R");
        straightRecursion(row,col - 1,board,myPosition,possibleMoves,"L");
        straightRecursion(row - 1,col,board,myPosition,possibleMoves,"D");
        straightRecursion(row + 1,col,board,myPosition,possibleMoves,"U");

    }


    public static void knightMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> possibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        knightHelper(row + 1,col + 2,board,myPosition,possibleMoves);
        knightHelper(row + 1,col - 2,board,myPosition,possibleMoves);
        knightHelper(row - 1,col + 2,board,myPosition,possibleMoves);
        knightHelper(row - 1,col - 2,board,myPosition,possibleMoves);
        knightHelper(row + 2,col + 1,board,myPosition,possibleMoves);
        knightHelper(row + 2,col - 1,board,myPosition,possibleMoves);
        knightHelper(row - 2,col + 1,board,myPosition,possibleMoves);
        knightHelper(row - 2,col - 1,board,myPosition,possibleMoves);
    }


    public static void pawnMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> possibleMoves) {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int colorMod = 1;
        if (myColor == ChessGame.TeamColor.BLACK) {colorMod = - 1;}
        ArrayList<ChessPiece.PieceType> promotions = new ArrayList<ChessPiece.PieceType>();
        if ((row + 1 == 8 && myColor == ChessGame.TeamColor.WHITE) || (row - 1 == 1 && myColor == ChessGame.TeamColor.BLACK)) {
            promotions.add(ChessPiece.PieceType.QUEEN);
            promotions.add(ChessPiece.PieceType.ROOK);
            promotions.add(ChessPiece.PieceType.BISHOP);
            promotions.add(ChessPiece.PieceType.KNIGHT);
        } else {
            promotions.add(null);}

        pawnHelper(row + colorMod, col, board, myPosition,possibleMoves, promotions, myColor);
        pawnHelperTake(row + colorMod, col + 1, board, myPosition,possibleMoves, promotions, myColor);
        pawnHelperTake(row + colorMod, col - 1, board, myPosition,possibleMoves, promotions, myColor);



        }}







