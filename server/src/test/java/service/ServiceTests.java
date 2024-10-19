package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeAll;

public class ServiceTests {

    static GameService gameService;
    static UserService userService;
    static GameDAO gameDAO;
    static UserDAO userDAO;
    static AuthDAO authDAO;

    @BeforeAll
    static void init() {
       gameDAO = new MemoryGameDAO();
       userDAO = new MemoryUserDAO();
       authDAO = new MemoryAuthDAO();
       gameService = new GameService(userDAO,authDAO,gameDAO);
       userService = new UserService(userDAO,authDAO);
    }



}
