package ChessClients;

import exception.ResponseException;
import model.GameData;
import model.JoinGameReq;
import server.ServerFacade;
import ui.*;
import java.util.Arrays;
import java.util.Collection;


public class LoginClient {
    private final String auth;
    private final ServerFacade server;
    public LoginClient(ServerFacade server, String auth) {
        this.server = server;
        this.auth = auth;
//        this.notificationHandler = notificationHandler;
    }



    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (cmd) {
                case "list" -> returnList();
                case "join" -> joinGame(params);
                case "observe" -> observeGame();
                case "create" -> createGame(params);
                case "logout" -> logout();

                default -> "HELP";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }

    public String observeGame() {
        return "We be lookin'";
    }

    public String returnList() throws ResponseException {
        Collection<GameData> retrievedList = server.listGames(auth);
        String finalVar = """
                CurrentGames
                """;
        finalVar += "\n";
        int num = 1;
        for (GameData item : retrievedList) {
            String whiteResult = item.whiteUsername() == null ? "empty" : item.whiteUsername();
            String blackResult = item.blackUsername() == null ? "empty" : item.blackUsername();

            finalVar += String.format("%d.\tGame: %s\tWhite: %s\tBlack: %s\t",
                    num,item.gameName(),whiteResult ,blackResult);
            finalVar += "\n";
            num+=1;
        }

        return finalVar;
    }


    public String createGame(String... params) throws ResponseException {
        server.createGame(params[0],auth);
        return "Game Created Successfully";
    }

    public String joinGame(String... params) throws ResponseException {
        Collection<GameData> allGames = server.listGames(auth);
        GameData[] gameArray = allGames.toArray(new GameData[0]);
        int id = Integer.parseInt(params[1]);

        GameData currentGame = gameArray[id - 1];
        int newID = currentGame.gameID();

//        JoinGameReq req = new JoinGameReq(params[0],newID);
//        server.joinGame(req, auth);



        return
                VisualizeBoard.produceWhiteBoard(currentGame.game().getBoard()) +
                VisualizeBoard.produceBlackBoard(currentGame.game().getBoard());
//        return String.format("Join Game %s", params[1]);
    }


    public String logout() throws ResponseException {
        server.logoutUser(auth);
        return "GOODBYE";
    }


}
