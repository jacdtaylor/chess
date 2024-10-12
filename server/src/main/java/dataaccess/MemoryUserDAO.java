package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{


    HashSet<UserData> storedUserData = new HashSet<UserData>();

    public UserData getUser(String username) throws DataAccessException {

        for (UserData data : storedUserData) {
            if (data.username().equals(username)) {
                return data;
            }

        }
        throw new DataAccessException("Username does not exist: " + username);
    }


    public void createUser(UserData user) {
        storedUserData.add(user);}

    public void clear() {
        storedUserData = new HashSet<UserData>();
    }
}
