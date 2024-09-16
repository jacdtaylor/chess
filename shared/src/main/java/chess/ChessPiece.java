package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static chess.MoveCalculator.*;

/**
 * Represents a single chess piece
 * <p>Test
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
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
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> PossibleMoves = new ArrayList<ChessMove>();

        switch (board.getPiece(myPosition).getPieceType()) {
            case BISHOP:
                BishopMoveCalculator(board,myPosition,PossibleMoves);
                return PossibleMoves;
            case ROOK:
                RookMoveCalculator(board,myPosition,PossibleMoves);
                return PossibleMoves;
            case QUEEN:
                QueenMoveCalculator(board,myPosition,PossibleMoves);
                return PossibleMoves;
            case KING:
                KingMoveCalculator(board,myPosition,PossibleMoves);
                return PossibleMoves;
            case KNIGHT:
                KnightMoveCalculator(board,myPosition,PossibleMoves);
                return PossibleMoves;
            case PAWN:
                PawnMoveCalculator(board,myPosition,PossibleMoves);
                return PossibleMoves;

        }
        return PossibleMoves;
    }
}
