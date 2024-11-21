package ui;

import chess.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class VisualizeBoard {


    static public String produceBlackBoard(ChessGame game, ChessPosition possibleMovesTarget) {

        ArrayList<ChessPosition> highlightedSpots = new ArrayList<ChessPosition>();
        ChessBoard board = game.getBoard();
        if (possibleMovesTarget != null) {
            for (ChessMove move : game.validMoves(possibleMovesTarget)) {
                highlightedSpots.add(move.getEndPosition());
            }


        } else {
            highlightedSpots = null;
        }

        String visualization = """
                """;
        String endLines = SET_TEXT_COLOR_WHITE + SET_BG_COLOR_BLACK +
                "   " + FULLWIDTHH + FULLWIDTHG + FULLWIDTHF + FULLWIDTHE
                +FULLWIDTHD + FULLWIDTHC + FULLWIDTHB + FULLWIDTHA + "   "
                + RESET_BG_COLOR + "\n";
        visualization += endLines;
        int y = 1;
        int x = 8;
        String currentBb = SET_BG_COLOR_BEIGE;
        String txtColor = SET_TEXT_COLOR_BLACK;
        String bgColor = currentBb;
        String pieceType = "";
        while (y <= 8) {
            String line = SET_BG_COLOR_BLACK + " " + Integer.toString(y) + " ";

            while (x >= 1) {
                ChessPosition currentPosition = new ChessPosition(y,x);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                pieceType = getPieceString(currentPiece);
                if (highlightedSpots != null &&
                        highlightedSpots.contains(new ChessPosition(y,x))) {
                    bgColor = (currentBb == SET_BG_COLOR_BEIGE) ? SET_BG_COLOR_RED : SET_BG_COLOR_LIGHT_RED;
                }else
                {bgColor = currentBb;}
                if (currentPosition.equals(possibleMovesTarget)) {txtColor = SET_TEXT_COLOR_RED;}
                else {txtColor = SET_TEXT_COLOR_BLACK;}


                line += bgColor +txtColor + pieceType + RESET_TEXT_COLOR;
                x--;

                if (currentBb.equals(SET_BG_COLOR_BEIGE) && x!=0) {
                    currentBb = SET_BG_COLOR_DARK_GREEN;
                } else if (x!=0) {
                    currentBb = SET_BG_COLOR_BEIGE;
                }
            }
            line+=SET_BG_COLOR_BLACK + " " + Integer.toString(y) + " "+ RESET_BG_COLOR+ "\n" ;
            visualization += line;
            x = 8;
            y++;
        }
        visualization += endLines;
        return visualization;
    }


    static public String produceWhiteBoard(ChessGame game, ChessPosition possibleMovesTarget) {
        ArrayList<ChessPosition> highlightedSpots = new ArrayList<ChessPosition>();
        ChessBoard board = game.getBoard();
        if (possibleMovesTarget != null) {
            for (ChessMove move : game.validMoves(possibleMovesTarget)) {
                highlightedSpots.add(move.getEndPosition());
            }


        } else {
            highlightedSpots = null;
        }

        String visualization = """
                """;
        String endLines = RESET_TEXT_COLOR+
                SET_BG_COLOR_BLACK +
                "   " + FULLWIDTHA + FULLWIDTHB + FULLWIDTHC + FULLWIDTHD
                +FULLWIDTHE + FULLWIDTHF + FULLWIDTHG + FULLWIDTHH + "   "
                + RESET_BG_COLOR + "\n";
        visualization += endLines;
        int y = 8;
        int x = 1;
        String currentBb = SET_BG_COLOR_BEIGE;
        String bgColor = currentBb;
        String txtColor = SET_TEXT_COLOR_BLACK;

        String pieceType = "";
        while (y >= 1) {
            String line = SET_BG_COLOR_BLACK + " " + Integer.toString(y) + " ";

            while (x <= 8) {
                ChessPosition currentPosition = new ChessPosition(y,x);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                pieceType = getPieceString(currentPiece);
                if (highlightedSpots != null &&
                        highlightedSpots.contains(new ChessPosition(y,x))) {
                    bgColor = (currentBb == SET_BG_COLOR_BEIGE) ? SET_BG_COLOR_RED : SET_BG_COLOR_LIGHT_RED;
                }
                else
                {bgColor = currentBb;}
                if (currentPosition.equals(possibleMovesTarget)) {txtColor = SET_TEXT_COLOR_RED;}
                else {txtColor = SET_TEXT_COLOR_BLACK;}
                line += bgColor + txtColor + pieceType + RESET_TEXT_COLOR;
                x++;

                if (currentBb.equals(SET_BG_COLOR_BEIGE) && x!=9) {
                    currentBb = SET_BG_COLOR_DARK_GREEN;
                } else if (x!=9) {
                    currentBb = SET_BG_COLOR_BEIGE;
                }
            }
            line+=SET_BG_COLOR_BLACK + " " + Integer.toString(y) + " "+ RESET_BG_COLOR+ "\n" ;
            visualization += line;
            x = 1;
            y--;
        }
        visualization += endLines;
        return visualization;
    }


    public static String getPieceString(ChessPiece piece) {
        String stringPiece = "";
        if (piece != null) {
            switch (piece.getPieceType()) {
                case BISHOP -> stringPiece = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                        WHITE_BISHOP : BLACK_BISHOP;
                case ROOK -> stringPiece = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                        WHITE_ROOK : BLACK_ROOK;
                case KNIGHT -> stringPiece = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                        WHITE_KNIGHT : BLACK_KNIGHT;
                case QUEEN -> stringPiece = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                        WHITE_QUEEN : BLACK_QUEEN;
                case KING -> stringPiece = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                        WHITE_KING : BLACK_KING;
                case PAWN -> stringPiece = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                        WHITE_PAWN : BLACK_PAWN;
            }} else {stringPiece = EMPTY;}
        return stringPiece;
    }

}
