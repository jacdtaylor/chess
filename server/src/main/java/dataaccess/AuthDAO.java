package dataaccess;

import exception.DataAccessException;
import model.AuthData;

public interface AuthDAO {

    void clear() throws DataAccessException;
    AuthData getAuth(String auth) throws DataAccessException;
    void deleteAuth(AuthData auth) throws DataAccessException;
    void createAuth(AuthData data);
    Boolean confirmAuthToken(String authToken) throws DataAccessException;
    public String getUserFromAuth(String auth) throws DataAccessException;
}
