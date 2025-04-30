package phonestore.service.exception;


//表示地址不存在的异常
public class AddressNotExistsException extends ServiceException {
    public AddressNotExistsException() {
        super();
    }

    public AddressNotExistsException(String message) {
        super(message);
    }

    public AddressNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressNotExistsException(Throwable cause) {
        super(cause);
    }

    protected AddressNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
