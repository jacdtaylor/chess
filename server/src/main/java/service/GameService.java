package service;





import model.*;
import dataaccess.*;
import org.eclipse.jetty.util.log.Log;

import java.util.UUID;

public class GameService {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public GameData joinGame(String authToken,String playerColor,int gameID) throws DataAccessException {
        authDAO.getAuth(authToken);
        GameData targetGame = gameDAO.getGame(gameID);


    }
    public GameData gameList(String authToken) {}
    public GameData createGame(String authToken, String GameID) {}

}
