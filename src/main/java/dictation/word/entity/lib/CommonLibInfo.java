package dictation.word.entity.lib;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonLibInfo {
    @JsonUnwrapped
    LibInfo lib;
    boolean hasLib;
    boolean self;
}
