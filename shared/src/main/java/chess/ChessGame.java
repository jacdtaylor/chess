package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.pieceMoves;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessGame.TeamColor turn;
    private ChessBoard board;


    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
        this.turn = TeamColor.WHITE;
    }



    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (getBoard().getPiece(startPosition) == null)  {return new ArrayList<ChessMove>();}
        ChessGame.TeamColor currentColor = getBoard().getPiece(startPosition).getTeamColor();
        ArrayList<ChessMove> preCheckMoves = (ArrayList<ChessMove>) pieceMoves(getBoard(), startPosition);
        ArrayList<ChessMove> CheckMoves = new ArrayList<ChessMove>();

        for (ChessMove possibleMove: preCheckMoves) {
            ChessBoard copy = new ChessBoard(getBoard());
            makePossibleMove(possibleMove,copy);
            if (!isInCheckHelper(currentColor,copy)) {
                CheckMoves.add(possibleMove);
            }

        }
        return CheckMoves;
    }

    public void makePossibleMove(ChessMove move, ChessBoard boardCopy) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        if (boardCopy.getPiece(start) == null) {return;}
        boardCopy.addPiece(end,boardCopy.getPiece(start));
        boardCopy.addPiece(start,null);
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        TeamColor currentTurn= getBoard().getPiece(start).getTeamColor();
        if (validMoves(start).contains(move)) {
            getBoard().addPiece(end,getBoard().getPiece(start));
            getBoard().addPiece(start,null);
            if (move.getPromotionPiece() != null) {getBoard().getPiece(end).Promote(move.getPromotionPiece());}

            if (currentTurn == TeamColor.WHITE) {setTeamTurn(TeamColor.BLACK);}
            else {setTeamTurn(TeamColor.WHITE);}
        } else {throw new InvalidMoveException();}




    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        ChessBoard PossibleBoard = getBoard();
        for (int y = 1; y <=8; y++) {
            for (int x = 1; x<= 8; x++) {
                if (PossibleBoard.getPiece(new ChessPosition(y,x))!=null &&
                        PossibleBoard.getPiece(new ChessPosition(y,x)).getTeamColor() != teamColor) {
                    for (ChessMove move : pieceMoves(PossibleBoard,new ChessPosition(y,x)))
                    {
                        ChessPosition examinedMove = move.getEndPosition();
                        if (PossibleBoard.getPiece(examinedMove) != null &&
                                PossibleBoard.getPiece(examinedMove).getPieceType() == ChessPiece.PieceType.KING &&
                                PossibleBoard.getPiece(examinedMove).getTeamColor() == teamColor) return true;
                    }
                }}
        }

        return false;

    }
    public boolean isInCheckHelper(TeamColor teamColor, ChessBoard PossibleBoard) {

        for (int y = 1; y <=8; y++) {
            for (int x = 1; x<= 8; x++) {
                if (PossibleBoard.getPiece(new ChessPosition(y,x))!=null &&
                        PossibleBoard.getPiece(new ChessPosition(y,x)).getTeamColor() != teamColor) {
                for (ChessMove move : pieceMoves(PossibleBoard,new ChessPosition(y,x)))
                {
                    ChessPosition examinedMove = move.getEndPosition();
                    if (PossibleBoard.getPiece(examinedMove) != null &&
                            PossibleBoard.getPiece(examinedMove).getPieceType() == ChessPiece.PieceType.KING &&
                            PossibleBoard.getPiece(examinedMove).getTeamColor() == teamColor) return true;
                }
            }}
        }

        return false;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        for (int y = 1; y <=8; y++) {
            for (int x = 1; x<= 8; x++) {
                ChessPosition current = new ChessPosition(y,x);
                if (getBoard().getPiece(current) != null
                && getBoard().getPiece(current).getTeamColor() == teamColor) {
                    if (!validMoves(current).isEmpty()) {return false;}
                }
                }
            }


        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int y = 1; y <=8; y++) {
            for (int x = 1; x<= 8; x++) {
                ChessPosition current = new ChessPosition(y,x);
                if (getBoard().getPiece(current) != null
                        && getBoard().getPiece(current).getTeamColor() == teamColor) {
                    if (!validMoves(current).isEmpty()) {return false;}
                }
            }
        }


        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) { this.board = board;

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
