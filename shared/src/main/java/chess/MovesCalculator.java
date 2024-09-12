package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesCalculator {




    public static void DiagonalRecursion(int row, int col, ChessBoard board, ChessPosition startPosition, String direction,
                                         ArrayList<ChessMove> PossibleMoves, ChessGame.TeamColor currentColor) {
        if (row == 0 || row == 9 || col == 0 || col == 9) {
            return;
        }

        ChessPosition endPosition = new ChessPosition(row,col);

        if (startPosition.getRow() != row || startPosition.getColumn() != col) {
            ChessPiece target = board.getPiece(endPosition);

            if (target != null && target.getTeamColor() == currentColor) {return;}
            PossibleMoves.add(new ChessMove(startPosition,endPosition,null));
            if (target != null && target.getTeamColor() != currentColor) return; }

        if (direction == "LD") {
            DiagonalRecursion(--row,--col,board,startPosition,"LD", PossibleMoves,currentColor);
        }
        else if (direction == "RD") {
            DiagonalRecursion(--row,++col,board,startPosition,"RD", PossibleMoves,currentColor);
        }
        else if (direction == "LU") {
            DiagonalRecursion(++row,--col,board,startPosition,"LU", PossibleMoves,currentColor);
        }
        else {
            DiagonalRecursion(++row,++col,board,startPosition,"RU", PossibleMoves,currentColor);
        }


    }


    public static void StraightRecursion(int row, int col, ChessBoard board, ChessPosition startPosition, String direction,
                                         ArrayList<ChessMove> PossibleMoves, ChessGame.TeamColor currentColor) {
        if (row == 0 || row == 9 || col == 0 || col == 9) {
            return;
        }

        ChessPosition endPosition = new ChessPosition(row,col);

        if (startPosition.getRow() != row || startPosition.getColumn() != col) {
            ChessPiece target = board.getPiece(endPosition);

            if (target != null && target.getTeamColor() == currentColor) {return;}
            PossibleMoves.add(new ChessMove(startPosition,endPosition,null));
            if (target != null && target.getTeamColor() != currentColor) return; }

        if (direction == "L") {
            StraightRecursion(row,--col,board,startPosition,"L", PossibleMoves,currentColor);
        }
        else if (direction == "R") {
            StraightRecursion(row,++col,board,startPosition,"R", PossibleMoves,currentColor);
        }
        else if (direction == "U") {
            StraightRecursion(++row,col,board,startPosition,"U", PossibleMoves,currentColor);
        }
        else  if (direction == "D"){
            StraightRecursion(--row,col,board,startPosition,"D", PossibleMoves,currentColor);
        }


    }

    public static Collection<ChessMove> bishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> PossibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessPiece.PieceType currentType = currentPiece.getPieceType();
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();

        DiagonalRecursion(row,col,board,myPosition,"RU",PossibleMoves,currentColor);
        DiagonalRecursion(row,col,board,myPosition,"RD",PossibleMoves,currentColor);
        DiagonalRecursion(row,col,board,myPosition,"LD",PossibleMoves,currentColor);
        DiagonalRecursion(row,col,board,myPosition,"LU",PossibleMoves,currentColor);


    return PossibleMoves;
    }

    public static Collection<ChessMove> rookMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> PossibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessPiece.PieceType currentType = currentPiece.getPieceType();
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();

        StraightRecursion(row,col,board,myPosition,"R",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"L",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"D",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"U",PossibleMoves,currentColor);


        return PossibleMoves;
    }

    public static Collection<ChessMove> kingMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> PossibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessPiece.PieceType currentType = currentPiece.getPieceType();
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();

        StraightRecursion(row,col,board,myPosition,"R",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"L",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"D",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"U",PossibleMoves,currentColor);


        return PossibleMoves;
    }

    public static Collection<ChessMove> knightMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> PossibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessPiece.PieceType currentType = currentPiece.getPieceType();
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();

        StraightRecursion(row,col,board,myPosition,"R",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"L",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"D",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"U",PossibleMoves,currentColor);


        return PossibleMoves;
    }

    public static Collection<ChessMove> pawnMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> PossibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessPiece.PieceType currentType = currentPiece.getPieceType();
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();

        StraightRecursion(row,col,board,myPosition,"R",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"L",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"D",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"U",PossibleMoves,currentColor);


        return PossibleMoves;
    }

    public static Collection<ChessMove> queenMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> PossibleMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessPiece.PieceType currentType = currentPiece.getPieceType();
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();

        StraightRecursion(row,col,board,myPosition,"R",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"L",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"D",PossibleMoves,currentColor);
        StraightRecursion(row,col,board,myPosition,"U",PossibleMoves,currentColor);


        return PossibleMoves;
    }




}