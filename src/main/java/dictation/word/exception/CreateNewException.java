package dictation.word.exception;

/**
 * 新建班级异常
 *
 * @author ljh
 * @date 2022/11/21
 */
public class CreateNewException extends RuntimeException {
    public CreateNewException() {
    }

    public CreateNewException(String message) {
        super(message);
    }
}
