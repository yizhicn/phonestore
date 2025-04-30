package phonestore.service.exception;

public class DiscountInvalidException extends ServiceException {
    public DiscountInvalidException() {
        super();
    }

    public DiscountInvalidException(String message) {
        super(message);
    }

    public DiscountInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}