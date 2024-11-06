package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameReq;
import model.UserData;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public AuthData registerUser(UserData user) throws Exception {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData loginUser(UserData user) throws Exception {
        var path = "/session";
        return this.makeRequest("DELETE", path, user, AuthData.class, null);
    }

    public void logoutUser(String authToken) throws Exception {
        var path = "/session";
        this.makeRequest("DELETE", path, authToken, AuthData.class, null);
    }

    public void clearDB() throws Exception {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public GameData[] listGames(String auth) throws Exception {
        var path = "/game";
        record listGameDataResponse(GameData[] gameData) {
        }
        var response = this.makeRequest("GET", path, null, listGameDataResponse.class, auth);
        return response.gameData();
    }

    public int createGame(String gameName, String auth) throws Exception {
        var path = "/game";
        record createGameDataResponse(int id) {
        }
        var response = this.makeRequest("POST", path, gameName, createGameDataResponse.class, auth);
        return response.id();
    }

    public void joinGame(JoinGameReq req,String auth) throws Exception {
        var path = "/game";
        this.makeRequest("PUT",path,req,null,auth);
    }



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String auth) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
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
            throw new ResponseException(status, "failure: " + status);
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