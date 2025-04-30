package phonestore.service.exception;

public class CategoryNotExistsException extends RuntimeException {
    public CategoryNotExistsException() {
        super();
    }

    public CategoryNotExistsException(String message) {
        super(message);
    }

    public CategoryNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}