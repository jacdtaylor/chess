package dataaccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{


    HashSet<UserData> storedUserData = new HashSet<UserData>();


    @Override
    public boolean confirmUsername(String username) {
        for (UserData data : storedUserData) {
            if (data.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {

        for (UserData data : storedUserData) {
            if (data.username().equals(username)) {
                return data;
            }

        }
        throw new DataAccessException("Username does not exist: " + username);
    }


    @Override
    public void createUser(UserData user) {


        storedUserData.add(user);}


    @Override
    public void clear() {
        storedUserData = new HashSet<UserData>();
    }

    @Override
    public boolean validateCredentials(UserData user) throws DataAccessException, InvalidCredentialException{
        UserData currentCreds = getUser(user.username());
        if (user.password().equals(currentCreds.password())) {return true;}
        else {
            throw new InvalidCredentialException("Password does not match");
        }
    }
}


