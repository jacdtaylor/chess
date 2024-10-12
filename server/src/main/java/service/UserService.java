package service;

import model.AuthData;
import model.UserData;
import dataaccess.*;

public class UserService {


    UserDAO userDAO;
    AuthDAO authDAO;

    public AuthData register(UserData user) {


    }
    public AuthData login(UserData user) throws DataAccessException {
        UserData currentUser = userDAO.getUser(user.username());


    }
    public void logout(AuthData auth) {


    }
}