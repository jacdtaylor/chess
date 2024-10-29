package Exceptions;

public class GameManagerError extends RuntimeException {
    public GameManagerError(String message) {
        super(message);
    }
}
