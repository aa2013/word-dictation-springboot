package dictation.word.exception;

/**
 * token过期异常
 * @author ljh
 */
public class NoPermissionException extends RuntimeException{

    public NoPermissionException() {
    }

    public NoPermissionException(String message) {
        super(message);
    }
}
