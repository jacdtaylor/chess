package dataaccess;

import model.AuthData;

import javax.xml.crypto.Data;

public interface AuthDAO {

    void clear();
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void createAuth(AuthData data);

}
