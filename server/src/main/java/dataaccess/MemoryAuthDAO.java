package dataaccess;
import java.util.HashMap;

import Exceptions.DataAccessException;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    private final HashMap<String, AuthData> authTokenKey = new HashMap<>();
    private final HashMap<String, AuthData> userTokenKey = new HashMap<>();





    @Override
    public Boolean confirmAuthToken(String authToken) {
        return authTokenKey.containsKey(authToken);}

    @Override
    public void createAuth(AuthData data) {
        userTokenKey.put(data.username(), data);
        authTokenKey.put(data.authToken(),data);
    }
    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {
        userTokenKey.remove(auth.username());
        authTokenKey.remove(auth.authToken());
    }
    @Override
    public AuthData getAuth(String auth) throws DataAccessException {
        AuthData pulledAuth = authTokenKey.get(auth);
        if (pulledAuth != null) {return pulledAuth;}
        throw new DataAccessException("Auth Token does not exist: " + auth);}

    @Override
    public String getUserFromAuth(String auth) {
        AuthData pulledAuth = authTokenKey.get(auth);
        return pulledAuth.username();
    }

    @Override
    public void clear() {authTokenKey.clear();
                        userTokenKey.clear();}


}



