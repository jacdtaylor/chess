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

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO =userDAO;
        this.authDAO =authDAO;
        this.gameDAO =gameDAO;

    }
    public GameData joinGame(String authToken,JoinGameReq gameReq) throws DataAccessException, GameManagerError, UnauthorizationException {
        try {authDAO.getAuth(authToken);}catch(DataAccessException e) {throw new UnauthorizationException("Unauthorized Access");}

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


    public Collection<GameData> gameList(String authToken) throws UnauthorizationException {

        if (authDAO.confirmAuthToken(authToken)) {
        return gameDAO.listGames();} else {
            throw new UnauthorizationException("Unauthorized Access");
        }
    }




    public int createGame(String authToken, String gameName) throws UnauthorizationException {
//        return 8;
        if (!authDAO.confirmAuthToken(authToken)) {throw new UnauthorizationException("Unauthorized Access");}

        Random rand = new Random();
        boolean uniqueID = false;
        int n = rand.nextInt(1000);
        while (!uniqueID) {

            try {
                gameDAO.getGame(n);
                n = rand.nextInt(1000);
            } catch (DataAccessException e)
            {uniqueID = true;}
        }
            ChessGame createdGame = new ChessGame();
            ChessBoard createdBoard = new ChessBoard();
            createdBoard.resetBoard();
            createdGame.setBoard(createdBoard);


        gameDAO.createGame(new GameData(n, null, null, gameName, createdGame));

        return n;
    }

    public void clearAll() {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();
    }
}
