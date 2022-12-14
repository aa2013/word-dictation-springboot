package dictation.word.exception;

/**
 * 删除课程异常
 *
 * @author ljh
 * @date 2022/11/21
 */
public class DelException extends RuntimeException {
    public DelException() {
    }

    public DelException(String message) {
        super(message);
    }
}
