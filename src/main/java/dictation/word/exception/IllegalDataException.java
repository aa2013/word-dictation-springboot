package dictation.word.exception;

/**
 * 数据不合法异常
 *
 * @author ljh
 * @date 2022/11/21
 */
public class IllegalDataException extends RuntimeException {
    public IllegalDataException() {
    }

    public IllegalDataException(String message) {
        super(message);
    }
}
