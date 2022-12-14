package dictation.word.exception;

/**
 * token过期异常
 * @author ljh
 */
public class TokenExpireException extends RuntimeException{

    public TokenExpireException() {
    }

    public TokenExpireException(String message) {
        super(message);
    }
}
