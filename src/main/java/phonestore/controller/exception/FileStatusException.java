package phonestore.controller.exception;

//处理文件上传状态异常
public class FileStatusException extends FileUploadException {

    public FileStatusException() {
        super();
    }

    public FileStatusException(String message) {
        super(message);
    }

    public FileStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileStatusException(Throwable cause) {
        super(cause);
    }

    protected FileStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
