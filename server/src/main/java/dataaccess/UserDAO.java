package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.util.HashSet;

public interface UserDAO {

    UserData getUser(String username) throws DataAccessException;

    void createUser(UserData user);

    void clear();
    Boolean validateCredentials(UserData user) throws DataAccessException, InvalidCredentialException;
}
