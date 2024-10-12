package service;

import model.AuthData;
import model.UserData;
import dataaccess.*;
import org.eclipse.jetty.util.log.Log;

import java.util.UUID;

public class UserService {


    UserDAO userDAO;
    AuthDAO authDAO;

    public AuthData register(UserData user) throws DataAccessException {
        if (userDAO.confirmUsername(user.username())) {
            throw new LoginException("User already exists");
        }
        userDAO.createUser(user);
        return login(user);
    }


    public AuthData login(UserData user) throws DataAccessException, InvalidCredentialException {
        if (userDAO.validateCredentials(user)) {
            String auth = UUID.randomUUID().toString();
            if (!authDAO.confirmAuth(user.username())) {
                throw new LoginException("User already logged in");
            }
            AuthData newAuthData = new AuthData(auth, user.username());
            authDAO.createAuth(newAuthData);
            return newAuthData;
        }
        throw new InvalidCredentialException("Username does not exist");
    }

    public void logout(AuthData auth) throws DataAccessException {
        AuthData currentAuth = authDAO.getAuth(auth.authToken());
        authDAO.deleteAuth(auth);

        }
    }
