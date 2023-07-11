package dictation.word.exception;

public class EmailFormatException extends RuntimeException {
    public EmailFormatException() {
    }

    public EmailFormatException(String message) {
        super(message);
    }

}
