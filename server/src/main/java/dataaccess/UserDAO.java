package dataaccess;

import Exceptions.DataAccessException;
import Exceptions.InvalidCredentialException;
import model.UserData;

public interface UserDAO {



    UserData getUser(String username) throws DataAccessException;

    void createUser(UserData user) throws DataAccessException;
    boolean confirmUsername(String username) throws DataAccessException;
    void clear() throws DataAccessException;
    boolean validateCredentials(UserData user) throws DataAccessException, InvalidCredentialException;
}
