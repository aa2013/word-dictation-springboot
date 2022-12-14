package dictation.word.exception;

/**
 * 更改课程名字异常
 *
 * @author ljh
 * @date 2022/11/21
 */
public class UpdateException extends RuntimeException {
    public UpdateException() {
    }

    public UpdateException(String message) {
        super(message);
    }
}
