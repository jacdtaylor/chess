package service;

import Exceptions.DataAccessException;
import Exceptions.GameManagerError;
import Exceptions.InvalidCredentialException;
import Exceptions.LoginException;
import model.AuthData;
import model.UserData;
import dataaccess.*;

import java.util.UUID;

public class UserService {


    private final UserDAO userDAO;
    private final AuthDAO authDAO;


    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;


    }
    public AuthData register(UserData user) throws DataAccessException, LoginException {
        if (userDAO.confirmUsername(user.username())) {
            throw new GameManagerError("User already exists");
        }
        userDAO.createUser(user);
        return login(user);
    }


    public AuthData login(UserData user) throws DataAccessException, InvalidCredentialException, LoginException {
        if (userDAO.validateCredentials(user)) {
            String auth = UUID.randomUUID().toString();

            AuthData newAuthData = new AuthData(auth, user.username());
            authDAO.createAuth(newAuthData);
            return newAuthData;
        }
        throw new DataAccessException("Password does not match");
    }

    public void logout(String auth) throws DataAccessException {
        AuthData currentAuth = authDAO.getAuth(auth);
        authDAO.deleteAuth(currentAuth);

        }
    }
