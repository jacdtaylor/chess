import chess.*;
import Exceptions.DataAccessException;
import server.*;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server test = new Server();
        test.run(8080);


    }
}