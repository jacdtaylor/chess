package dataaccess;
import java.util.HashSet;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    HashSet<AuthData> storedAuthData = new HashSet<AuthData>();


    @Override
    public void createAuth(AuthData data) {
        storedAuthData.add(data);
    }
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        AuthData target = getAuth(authToken);
        storedAuthData.remove(target);
    }
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData data : storedAuthData) {
            if (data.authToken().equals(authToken)) {
                return data;
            }

        }
        throw new DataAccessException("Auth Token does not exist: " + authToken);
    }
    @Override
    public void clear() {storedAuthData = new HashSet<AuthData>();}

}
