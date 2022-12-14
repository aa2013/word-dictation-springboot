package dictation.word.entity.book;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Book {
    @TableId(type = IdType.AUTO)
    Integer id;
    String bookName;
    String creator;
    Boolean common = false;
}
