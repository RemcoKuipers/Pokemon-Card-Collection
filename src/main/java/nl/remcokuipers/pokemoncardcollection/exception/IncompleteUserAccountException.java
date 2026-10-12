package nl.remcokuipers.pokemoncardcollection.exception;

public class IncompleteUserAccountException extends RuntimeException {
    public IncompleteUserAccountException(String message) {
        super(message);
    }
}
