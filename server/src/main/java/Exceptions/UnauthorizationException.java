package Exceptions;

public class UnauthorizationException extends RuntimeException {
    public UnauthorizationException(String message) {
        super(message);
    }
}
