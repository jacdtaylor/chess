package service;





import java.util.*;

import chess.ChessBoard;
import chess.ChessGame;
import model.*;
import dataaccess.*;
import org.eclipse.jetty.util.log.Log;

import javax.xml.crypto.Data;

public class GameService {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    int gameID = 0;
    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO =userDAO;
        this.authDAO =authDAO;
        this.gameDAO =gameDAO;

    }
    public GameData joinGame(String authToken,JoinGameReq gameReq) throws DataAccessException, GameManagerError {
        try {authDAO.getAuth(authToken);}catch(Exception e) {throw new DataAccessException("Unauthorized Access");}

        int gameID = gameReq.gameID();
        String color = gameReq.playerColor().toUpperCase();
        if (!color.equals("WHITE") && !color.equals("BLACK")) {throw new GameManagerError("Invalid Color");}
        GameData targetGame = gameDAO.getGame(gameID);
//        return targetGame;
        GameData joinedGame;

        if (color.equals("WHITE")) {
            if (targetGame.whiteUsername() == null) {

                joinedGame  = new GameData(gameID,authDAO.getUserFromAuth(authToken) ,targetGame.blackUsername(),
                        targetGame.gameName(), targetGame.game());
                gameDAO.updateGame(joinedGame);
                return joinedGame;
            }
            else {throw new GameManagerError("WHITE PLAYER TAKEN");}

        } else {if (targetGame.blackUsername() == null) {

                joinedGame  = new GameData(gameID, targetGame.whiteUsername(), authDAO.getUserFromAuth(authToken),
                        targetGame.gameName(), targetGame.game());
                gameDAO.updateGame(joinedGame);
                return joinedGame;

        }
        else {throw new GameManagerError("BLACK PLAYER TAKEN");}}


    }


    public Collection<GameData> gameList(String authToken) throws DataAccessException {

        if (authDAO.confirmAuthToken(authToken)) {
        return gameDAO.listGames();} else {
            throw new DataAccessException("Unauthorized Access");
        }
    }




    public int createGame(String authToken, String gameName) throws DataAccessException {
//        return 8;
        if (!authDAO.confirmAuthToken(authToken)) {throw new DataAccessException("Unauthorized Access");}

        int n = gameID + 1;
        gameID = gameID + 1;

            ChessGame createdGame = new ChessGame();
            ChessBoard createdBoard = new ChessBoard();
            createdBoard.resetBoard();
            createdGame.setBoard(createdBoard);


        gameDAO.createGame(new GameData(n, null, null, gameName, createdGame));

        return n;
    }

    public void clearAll() throws DataAccessException {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();
    }
}
