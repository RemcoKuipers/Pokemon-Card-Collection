package nl.remcokuipers.pokemoncardcollection.exception;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message,  Throwable cause) {
        super(message, cause);
    }
}
