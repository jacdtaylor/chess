package dataaccess;

import exception.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessUserTests {

    static UserDAO userDAO;




    @BeforeAll
    static void init() {
        userDAO = new SqlUserDAO();}


    @BeforeEach
    void clearGame() throws DataAccessException {
        userDAO.clear();

    }


    @Test
    @DisplayName("Clear Test")
    void clearTest() throws DataAccessException {
        userDAO.createUser(new UserData("username","password", "email"));
        userDAO.clear();
        UserData data = userDAO.getUser("username");
        assertNull(data);


    }

    @Test
    @DisplayName("Create User -")
    void createUserNeg() throws DataAccessException {
        userDAO.createUser(new UserData("username","password", "email"));
        assertThrows(Exception.class, ()->{userDAO.createUser(new UserData("username","password", "email"));});

    }

    @Test
    @DisplayName("Create User +")
    void createUserPos() throws DataAccessException {
        userDAO.createUser(new UserData("username","password", "email"));
        assertNotNull(userDAO.getUser("username"));

    }

    @Test
    @DisplayName("Get Username +")
    void getUsernamePos() throws DataAccessException {
        userDAO.createUser(new UserData("username","password", "email"));
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
        userDAO.createUser(new UserData("username","password", "email"));
        assertTrue(userDAO.validateCredentials(new UserData("username","password", "email")));

    }

    @Test
    @DisplayName("Validate Credentials +")
    void validateNeg() throws DataAccessException {
        userDAO.createUser(new UserData("username","password", "email"));
        assertFalse(userDAO.validateCredentials(new UserData("username","fake", "email")));
    }

    @Test
    @DisplayName("Confirm Username +")
    void confirmUserPos() throws DataAccessException {
        userDAO.createUser(new UserData("username","password", "email"));
        assertTrue(userDAO.confirmUsername("username"));
    }

    @Test
    @DisplayName("Confirm Username -")
    void confirmUserNeg() throws DataAccessException {
        assertFalse(userDAO.confirmUsername("user"));
    }



}
