package utility;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameReq;
import model.UserData;

import java.io.*;
import java.net.*;
import java.util.Collection;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public String getUser(String authToken) {
        var path = "/name";

        var response = this.makeRequest("PUT", path, null,String.class,authToken);
        return response;
    }

    public AuthData registerUser(UserData user) throws ResponseException {
        var path = "/user";
        AuthData response = this.makeRequest("POST", path, user, AuthData.class, null);
        return response;
    }

    public AuthData loginUser(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public void logoutUser(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }


    public Collection<GameData> listGames(String auth) throws ResponseException {
        var path = "/game";
        record ListGameDataResponse(Collection<GameData> games) {
        }
        var response = this.makeRequest("GET", path, null, ListGameDataResponse.class, auth);
        return response.games();
    }

    public int createGame(String gameName, String auth) throws ResponseException {
        var path = "/game";
        record CreateGameDataResponse(int id) {
        }
        GameData gameReq = new GameData(-999,null,null,gameName,null);
        var response = this.makeRequest("POST", path, gameReq, CreateGameDataResponse.class, auth);
        return response.id();
    }

    public void joinGame(JoinGameReq req,String auth) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT",path,req,null,auth);
    }

    public void clearAll() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE",path,null,null,null);
    }

    public GameData updateGame(GameData game) {
        var path= "/update";
        var response = this.makeRequest("PUT",path,game,GameData.class,null);
        return response;
    }

    public GameData getGame(int gameID) {
        var path = "/current";
        var response = this.makeRequest("PUT", path, gameID, GameData.class, null);
        return response;
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String auth) throws ResponseException {
        try {

            URL url = (new URI(serverUrl + path)).toURL();

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (auth != null) {
            http.setRequestProperty("authorization", auth); }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public String getServerUrl() {
        return serverUrl;
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException("failure");
        }
    }


    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}