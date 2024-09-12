package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessPiece.PieceType PieceType;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    this.PieceType = type;
    this.pieceColor = pieceColor;
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return PieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        PieceType CurrentPiece = board.getPiece(myPosition).getPieceType();


        return switch (CurrentPiece) {
            case KING -> MovesCalculator.kingMovesCalculator(board, myPosition);
            case QUEEN -> MovesCalculator.queenMovesCalculator(board, myPosition);
            case BISHOP -> MovesCalculator.bishopMovesCalculator(board, myPosition);
            case KNIGHT -> MovesCalculator.knightMovesCalculator(board, myPosition);
            case ROOK -> MovesCalculator.rookMovesCalculator(board, myPosition);
            case PAWN -> MovesCalculator.pawnMovesCalculator(board, myPosition);
        };

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece move = (ChessPiece) o;
        return PieceType.equals(move.PieceType) &&
                pieceColor.equals(move.pieceColor);
    }

    @Override
    public int hashCode() {
        return (17 * PieceType.hashCode()) + pieceColor.hashCode();
    }

    @Override
    public String toString() {
        return PieceType.toString();
    }
}
