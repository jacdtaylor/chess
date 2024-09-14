package chess;

import jdk.jshell.Diag;

import java.util.ArrayList;

public class MoveCalculator {



    public static void DiagonalRecursion(int row, int col, ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves, String direction) {
        if (row <=0 || col <= 0 || row >= 9 || col >= 9) {return;}
        ChessPosition endPosition = new ChessPosition(row,col);
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();

        if (board.getPiece(endPosition) != null &&
                board.getPiece(endPosition).getTeamColor().equals(myColor))
            {return;}
        PossibleMoves.add(new ChessMove(myPosition,endPosition,null));
        if (board.getPiece(endPosition) == null && board.getPiece(myPosition).getPieceType() != ChessPiece.PieceType.KING) {
            switch (direction) {
                case "UR":
                    DiagonalRecursion(++row,++col,board,myPosition,PossibleMoves,direction);
                    return;
                case "DR":
                    DiagonalRecursion(--row,++col,board,myPosition,PossibleMoves,direction);
                    return;
                case "DL":
                    DiagonalRecursion(--row,--col,board,myPosition,PossibleMoves,direction);
                    return;
                case "UL":
                    DiagonalRecursion(++row,--col,board,myPosition,PossibleMoves,direction);
                    return;
            }
        }
    }

    public static void StraightRecursion(int row, int col, ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves, String direction) {
        if (row <=0 || col <= 0 || row >= 9 || col >= 9) {return;}
        ChessPosition endPosition = new ChessPosition(row,col);
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();

        if (board.getPiece(endPosition) != null &&
                board.getPiece(endPosition).getTeamColor().equals(myColor))
        {return;}
        PossibleMoves.add(new ChessMove(myPosition,endPosition,null));
        if (board.getPiece(endPosition) == null && board.getPiece(myPosition).getPieceType() != ChessPiece.PieceType.KING) {

            switch (direction) {
                case "R":
                    StraightRecursion(row,++col,board,myPosition,PossibleMoves,direction);
                    return;
                case "D":
                    StraightRecursion(--row,col,board,myPosition,PossibleMoves,direction);
                    return;
                case "L":
                    StraightRecursion(row,--col,board,myPosition,PossibleMoves,direction);
                    return;
                case "U":
                    StraightRecursion(++row,col,board,myPosition,PossibleMoves,direction);
                    return;
            }
        }
    }

    public static void KnightHelper(int row, int col, ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves) {
        if (row <=0 || col <= 0 || row >= 9 || col >= 9) {return;}
        ChessPosition endPosition = new ChessPosition(row,col);
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        if (board.getPiece(endPosition) != null &&
                board.getPiece(endPosition).getTeamColor().equals(myColor))
        {return;}
        PossibleMoves.add(new ChessMove(myPosition,endPosition,null));
    }

    public static void PawnHelperTake(int row, int col, ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves, ArrayList<ChessPiece.PieceType> Promotion, ChessGame.TeamColor myColor) {
        if (row <=0 || col <= 0 || row >= 9 || col >= 9) {return;}
        ChessPosition endPosition = new ChessPosition(row,col);
        if (board.getPiece(endPosition) == null ||
                board.getPiece(endPosition).getTeamColor().equals(myColor))
        {return;}
        for (ChessPiece.PieceType Pro : Promotion) {
        PossibleMoves.add(new ChessMove(myPosition,endPosition,Pro));}
    }

    public static void PawnHelper(int row, int col, ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves, ArrayList<ChessPiece.PieceType> Promotion, ChessGame.TeamColor myColor) {
        if (row <=0 || col <= 0 || row >= 9 || col >= 9) {return;}
        ChessPosition endPosition = new ChessPosition(row,col);
        if (board.getPiece(endPosition) != null)
        {return;}
        for (ChessPiece.PieceType Pro : Promotion) {
            PossibleMoves.add(new ChessMove(myPosition,endPosition,Pro));}
        if (myColor == ChessGame.TeamColor.WHITE && row == 3) {
            PawnHelper(4,col,board,myPosition,PossibleMoves,Promotion,myColor);
        }
        if (myColor == ChessGame.TeamColor.BLACK && row == 6) {
            PawnHelper(5,col,board,myPosition,PossibleMoves,Promotion,myColor);
        }}

    public static void BishopMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        DiagonalRecursion(row + 1, col + 1, board, myPosition,PossibleMoves, "UR");
        DiagonalRecursion(row - 1, col - 1, board, myPosition,PossibleMoves, "DL");
        DiagonalRecursion(row + 1, col - 1, board, myPosition,PossibleMoves, "UL");
        DiagonalRecursion(row - 1, col + 1, board, myPosition,PossibleMoves, "DR");

    }

    public static void RookMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        StraightRecursion(row,col + 1,board,myPosition,PossibleMoves,"R");
        StraightRecursion(row,col - 1,board,myPosition,PossibleMoves,"L");
        StraightRecursion(row - 1,col,board,myPosition,PossibleMoves,"D");
        StraightRecursion(row + 1,col,board,myPosition,PossibleMoves,"U");
    }

    public static void QueenMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        DiagonalRecursion(row + 1, col + 1, board, myPosition,PossibleMoves, "UR");
        DiagonalRecursion(row - 1, col - 1, board, myPosition,PossibleMoves, "DL");
        DiagonalRecursion(row + 1, col - 1, board, myPosition,PossibleMoves, "UL");
        DiagonalRecursion(row - 1, col + 1, board, myPosition,PossibleMoves, "DR");
        StraightRecursion(row,col + 1,board,myPosition,PossibleMoves,"R");
        StraightRecursion(row,col - 1,board,myPosition,PossibleMoves,"L");
        StraightRecursion(row - 1,col,board,myPosition,PossibleMoves,"D");
        StraightRecursion(row + 1,col,board,myPosition,PossibleMoves,"U");

    }

    public static void KingMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        DiagonalRecursion(row + 1, col + 1, board, myPosition,PossibleMoves, "UR");
        DiagonalRecursion(row - 1, col - 1, board, myPosition,PossibleMoves, "DL");
        DiagonalRecursion(row + 1, col - 1, board, myPosition,PossibleMoves, "UL");
        DiagonalRecursion(row - 1, col + 1, board, myPosition,PossibleMoves, "DR");
        StraightRecursion(row,col + 1,board,myPosition,PossibleMoves,"R");
        StraightRecursion(row,col - 1,board,myPosition,PossibleMoves,"L");
        StraightRecursion(row - 1,col,board,myPosition,PossibleMoves,"D");
        StraightRecursion(row + 1,col,board,myPosition,PossibleMoves,"U");
    }

    public static void KnightMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        KnightHelper(row + 1,col + 2,board,myPosition,PossibleMoves);
        KnightHelper(row + 1,col - 2,board,myPosition,PossibleMoves);
        KnightHelper(row - 1,col + 2,board,myPosition,PossibleMoves);
        KnightHelper(row - 1,col - 2,board,myPosition,PossibleMoves);
        KnightHelper(row + 2,col + 1,board,myPosition,PossibleMoves);
        KnightHelper(row + 2,col - 1,board,myPosition,PossibleMoves);
        KnightHelper(row - 2,col + 1,board,myPosition,PossibleMoves);
        KnightHelper(row - 2,col - 1,board,myPosition,PossibleMoves);
    }

    public static void PawnMoveCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> PossibleMoves) {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int colorMod = 1;
        if (myColor == ChessGame.TeamColor.BLACK) {colorMod = - 1;}
        ArrayList<ChessPiece.PieceType> Promotions = new ArrayList<ChessPiece.PieceType>();
        if ((row + 1 == 8 && myColor == ChessGame.TeamColor.WHITE) || (row - 1 == 1 && myColor == ChessGame.TeamColor.BLACK)) {
            Promotions.add(ChessPiece.PieceType.QUEEN);
            Promotions.add(ChessPiece.PieceType.ROOK);
            Promotions.add(ChessPiece.PieceType.BISHOP);
            Promotions.add(ChessPiece.PieceType.KNIGHT);
        } else {Promotions.add(null);}

        PawnHelper(row + colorMod, col, board, myPosition,PossibleMoves, Promotions, myColor);
        PawnHelperTake(row + colorMod, col + 1, board, myPosition,PossibleMoves, Promotions, myColor);
        PawnHelperTake(row + colorMod, col - 1, board, myPosition,PossibleMoves, Promotions, myColor);



        }}







