package dictation.word.entity.lib;

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
public class LibInfo {
    Integer id;
    String libName;
    String creator;
    Boolean common = false;
}
