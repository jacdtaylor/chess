package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {

    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> PossibleMoves = new ArrayList<>();

        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        int y = row;
        for (var x = row; x <= 8; x++) {
            if (y == 9) {break;}


        ChessPosition endPos = new ChessPosition(x,y);
        PossibleMoves.add(new ChessMove(myPosition,endPos,null));
        y++;}


        return PossibleMoves;


    }
}
