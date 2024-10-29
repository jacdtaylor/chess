package exception;

public class NonexistantGame extends RuntimeException {
    public NonexistantGame(String message) {
        super(message);
    }
}
