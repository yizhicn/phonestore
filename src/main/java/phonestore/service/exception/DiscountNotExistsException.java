package phonestore.service.exception;

public class DiscountNotExistsException extends ServiceException {
    public DiscountNotExistsException() {
        super();
    }

    public DiscountNotExistsException(String message) {
        super(message);
    }

    public DiscountNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}