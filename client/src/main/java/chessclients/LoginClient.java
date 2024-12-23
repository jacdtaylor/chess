package chessclients;

import chess.ChessPosition;
import exceptions.ResponseException;
import model.GameData;
import model.JoinGameReq;
import utility.ServerFacade;
import ui.*;
import java.util.Arrays;
import java.util.Collection;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static utility.MoveInterpreter.translatePosition;


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
                case "observe" -> observeGame(params);
                case "create" -> createGame(params);
                case "logout" -> logout();

                default -> getHelp();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }



    }

    public String observeGame(String... params) {
            if (params.length < 1 ) {return SET_TEXT_COLOR_RED + "MISSING ARGUMENT" + RESET_TEXT_COLOR + "\n";
            }
            if (params.length > 2 ) {return SET_TEXT_COLOR_RED + "TOO MANY ARGUMENTS" + RESET_TEXT_COLOR + "\n";
            }

            try {
                Collection<GameData> allGames = server.listGames(auth);
                GameData[] gameArray = allGames.toArray(new GameData[0]);
                int id = Integer.parseInt(params[0]);

                GameData currentGame = gameArray[id - 1];
                int newID = currentGame.gameID();

                return "Observe Game " + params[0];



            }
            catch (ResponseException ex) {
                return SET_TEXT_COLOR_RED + "COLOR TAKEN" + RESET_TEXT_COLOR + "\n";}
            catch (Exception ex) {
                return SET_TEXT_COLOR_RED + "GAME DOES NOT EXIST" + RESET_TEXT_COLOR + "\n";

            }}


    public String returnList(String... params) {
        if (params.length > 0 ) {return SET_TEXT_COLOR_RED + "TOO MANY ARGUMENTS" + RESET_TEXT_COLOR + "\n";
        }
        try {
        Collection<GameData> retrievedList = server.listGames(auth);
        String finalVar = "";

        int num = 1;
        for (GameData item : retrievedList) {
            String whiteResult = item.whiteUsername() == null ? "empty" : item.whiteUsername();
            String blackResult = item.blackUsername() == null ? "empty" : item.blackUsername();

            finalVar += String.format("%d.\tGame: %s\tWhite: %s\tBlack: %s\t",
                    num,item.gameName(),whiteResult ,blackResult);
            finalVar += "\n";
            num+=1;
        }

        if (finalVar.isEmpty()) {return SET_TEXT_COLOR_RED + "NO GAMES HAVE BEEN CREATED" + RESET_TEXT_COLOR + "\n";}
        return finalVar;}
        catch (ResponseException ex) {
            return SET_TEXT_COLOR_RED + "UNAUTHORIZED ACCESS" + RESET_TEXT_COLOR + "\n";
        }
    }


    public String createGame(String... params) {

        if (params.length == 0) {return SET_TEXT_COLOR_RED + "MISSING ARGUMENT" + RESET_TEXT_COLOR + "\n";
        }
        if (params.length > 1 ) {return SET_TEXT_COLOR_RED + "TOO MANY ARGUMENTS" + RESET_TEXT_COLOR + "\n";
        }
        try {
        server.createGame(params[0], auth);
        return "Game Created Successfully\n";}
        catch (ResponseException ex) {
            return SET_TEXT_COLOR_RED + "UNAUTHORIZED ACCESS" + RESET_TEXT_COLOR + "\n";

        } catch (Exception ex) {
            return SET_TEXT_COLOR_RED + "ENTER GAME ID" + RESET_TEXT_COLOR + "\n";
        }
    }

    public String joinGame(String... params)  {
        if (params.length < 2 ) {return SET_TEXT_COLOR_RED + "MISSING ARGUMENT" + RESET_TEXT_COLOR + "\n";
        }
        if (params.length > 3 ) {return SET_TEXT_COLOR_RED + "TOO MANY ARGUMENTS" + RESET_TEXT_COLOR + "\n";
        }
        if (!params[0].equals("white") & !params[0].equals("black"))
        {return SET_TEXT_COLOR_RED + "INVALID COLOR" + RESET_TEXT_COLOR + "\n";}
        try {
            Collection<GameData> allGames = server.listGames(auth);
            GameData[] gameArray = allGames.toArray(new GameData[0]);
            int id = Integer.parseInt(params[1]);

            GameData currentGame = gameArray[id - 1];
            int newID = currentGame.gameID();

        JoinGameReq req = new JoinGameReq(params[0],newID);
        server.joinGame(req, auth);


            return "Join Game " + params[1] + " " + params[0];



        }
        catch (ResponseException ex) {
        return SET_TEXT_COLOR_RED + "COLOR TAKEN" + RESET_TEXT_COLOR + "\n";}
        catch (Exception ex) {
            return SET_TEXT_COLOR_RED + "GAME DOES NOT EXIST" + RESET_TEXT_COLOR + "\n";

    }}


    public String logout() throws ResponseException {
        server.logoutUser(auth);
        return "GOODBYE\n";
    }

    public String getHelp() {
        return
                """
                list
                join <WHITE/BLACK> <GAME #>
                create <GAMENAME>
                observe <GAME #>
                help
                logout
                """
                ;
    }

}
