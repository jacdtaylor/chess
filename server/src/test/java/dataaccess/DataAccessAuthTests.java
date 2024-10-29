package dataaccess;


import exception.DataAccessException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import model.AuthData;

public class DataAccessAuthTests {

    static AuthDAO authDAO;




    @BeforeAll
    static void init() {
        authDAO = new SqlAuthDAO();}


    @BeforeEach
    void clearGame() throws DataAccessException {
        authDAO.clear();
    }


    @Test
    @DisplayName("Clear Test Auth")
    void clearTest() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "user"));
        authDAO.clear();
        assertThrows(Exception.class, ()->{authDAO.getAuth("token");});
    }


    @Test
    @DisplayName("getAuth +")
    void getAuthPos() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "user"));
        AuthData auth = authDAO.getAuth("token");
        assertEquals(auth.authToken(),"token");
    }

    @Test
    @DisplayName("getAuth -")
    void getAuthNeg() throws DataAccessException {
        assertThrows(Exception.class, ()->{authDAO.getAuth("token");});

    }

    @Test
    @DisplayName("Delete Auth +")
    void deleteAuthPos() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "user"));
        authDAO.deleteAuth(new AuthData("token", "user"));

        assertThrows(Exception.class, ()->{authDAO.getAuth("token");});

    }

    @Test
    @DisplayName("Delete Auth -")
    void deleteAuthNeg() throws DataAccessException {
        assertThrows(Exception.class, ()->{authDAO.deleteAuth(new AuthData("token", "user"));});
    }

    @Test
    @DisplayName("Create Auth +")
    void createAuthPos() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "user"));
        AuthData auth = authDAO.getAuth("token");
        assertEquals(auth.authToken(),"token");
    }

    @Test
    @DisplayName("Create Auth -")
    void createAuthNeg() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "user"));
        assertThrows(Exception.class, ()->{ authDAO.createAuth(new AuthData("token", "user"));});
    }

    @Test
    @DisplayName("Confirm Auth +")
    void confirmAuthPos() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "user"));
        assertTrue(authDAO.confirmAuthToken("token"));
    }

    @Test
    @DisplayName("Confirm Auth -")
    void confirmAuthNeg() throws DataAccessException {
        assertThrows(Exception.class, ()->{  assertFalse(authDAO.confirmAuthToken("token"));});

    }

    @Test
    @DisplayName("GetUserAuth +")
    void getUserAuthPos() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "user"));
        assertEquals(authDAO.getUserFromAuth("token"), "user");
    }

    @Test
    @DisplayName("GetUserAuth -")
    void getUserAuthNeg() throws DataAccessException {
        assertThrows(Exception.class, ()->{authDAO.getUserFromAuth("token");});
    }

}
