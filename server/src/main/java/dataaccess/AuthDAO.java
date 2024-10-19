package dataaccess;

import model.AuthData;

import javax.xml.crypto.Data;

public interface AuthDAO {

    void clear();
    AuthData getAuth(String auth) throws DataAccessException;
    void deleteAuth(AuthData auth) throws DataAccessException;
    void createAuth(AuthData data);
    Boolean confirmAuthToken(String authToken);
    public String getUserFromAuth(String auth);
}
