package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessUserTests {

    static GameDAO gameDAO;
    static UserDAO userDAO;
    static AuthDAO authDAO;




    @BeforeAll
    static void init() {
        gameDAO = new SqlGameDAO();
        userDAO = new SqlUserDAO();
        authDAO = new SqlAuthDAO();
    }


    @BeforeEach
    void clearGame() throws DataAccessException {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();

    }


    @Test
    @DisplayName("Clear Test")
    void clearTest() throws DataAccessException {
        userDAO.createUser(new UserData("username","password"));
        userDAO.clear();
        UserData data = userDAO.getUser("username");
        assertNull(data);


    }

    @Test
    @DisplayName("Create User -")
    void createUserNeg() throws DataAccessException {
        userDAO.createUser(new UserData("username","password"));
        assertThrows(Exception.class, ()->{userDAO.createUser(new UserData("username","password"));});

    }

    @Test
    @DisplayName("Create User +")
    void createUserPos() throws DataAccessException {
        userDAO.createUser(new UserData("username","password"));
        assertNotNull(userDAO.getUser("username"));

    }

    @Test
    @DisplayName("Get Username +")
    void getUsernamePos() throws DataAccessException {
        userDAO.createUser(new UserData("username","password"));
        UserData data = userDAO.getUser("username");
        assertTrue(BCrypt.checkpw("password", data.password()));
    }


    @Test
    @DisplayName("Get Username -")
    void getUsernamepos() throws DataAccessException {
        assertNull(userDAO.getUser("username"));

    }
    @Test
    @DisplayName("Validate Credentials +")
    void validatePos() throws DataAccessException {
        userDAO.createUser(new UserData("username","password"));
        assertTrue(userDAO.validateCredentials(new UserData("username","password")));

    }

    @Test
    @DisplayName("Validate Credentials +")
    void validateNeg() throws DataAccessException {
        userDAO.createUser(new UserData("username","password"));
        assertFalse(userDAO.validateCredentials(new UserData("username","fake")));
    }

    @Test
    @DisplayName("Confirm Username +")
    void confirmUserPos() throws DataAccessException {
        userDAO.createUser(new UserData("username","password"));
        assertTrue(userDAO.confirmUsername("username"));
    }

    @Test
    @DisplayName("Confirm Username -")
    void confirmUserNeg() throws DataAccessException {
        assertFalse(userDAO.confirmUsername("user"));
    }



}
