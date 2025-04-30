package phonestore.service.exception;

//表示原密码不正确的异常
public class OriginalPasswordNotMatchException extends ServiceException{
    public OriginalPasswordNotMatchException() {
        super();
    }

    public OriginalPasswordNotMatchException(String message) {
        super(message);
    }

    public OriginalPasswordNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public OriginalPasswordNotMatchException(Throwable cause) {
        super(cause);
    }

    protected OriginalPasswordNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
