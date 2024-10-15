package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{


    private final HashMap<String, UserData> userDataHash = new HashMap<>();


    @Override
    public boolean confirmUsername(String username) {return userDataHash.containsKey(username);}

    @Override
    public UserData getUser(String username) throws DataAccessException {
    UserData pulledUser = userDataHash.get(username);
    if (pulledUser != null) {return pulledUser;}
    throw new DataAccessException("User does not exist");

    }


    @Override
    public void createUser(UserData user) {
        userDataHash.put(user.username(),user);}


    @Override
    public void clear() {
        userDataHash.clear();
    }

    @Override
    public boolean validateCredentials(UserData user) throws DataAccessException, InvalidCredentialException{
        UserData userRecord = userDataHash.get(user.username());
        if (userRecord == null) {
            throw new DataAccessException("User does not exist");
        }
        if (userRecord.password().equals(user.password())) {return true;}
        throw new InvalidCredentialException("Password does not match");
    }
}


