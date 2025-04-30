package phonestore.service.exception;

//表示查询的cart数据不存在
public class CartInfoNotExistsException extends ServiceException{
    public CartInfoNotExistsException() {
        super();
    }

    public CartInfoNotExistsException(String message) {
        super(message);
    }

    public CartInfoNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartInfoNotExistsException(Throwable cause) {
        super(cause);
    }

    protected CartInfoNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
