package dictation.word.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnavailableException extends RuntimeException{
    public UnavailableException(String msg){
        super(msg);
    }
}
