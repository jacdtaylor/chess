package dataaccess;
import java.util.HashSet;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    HashSet<AuthData> storedAuthData = new HashSet<AuthData>();


    @Override
    public Boolean confirmAuth(String username) {
        for (AuthData data : storedAuthData) {
            if (data.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void createAuth(AuthData data) {
        storedAuthData.add(data);
    }
    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {
        storedAuthData.remove(auth);
    }
    @Override
    public AuthData getAuth(String auth) throws DataAccessException {
        for (AuthData data : storedAuthData) {
            if (data.authToken().equals(auth)) {
                return data;
            }

        }
        throw new DataAccessException("Auth Token does not exist: " + auth);
    }
    @Override
    public void clear() {storedAuthData = new HashSet<AuthData>();}

}
