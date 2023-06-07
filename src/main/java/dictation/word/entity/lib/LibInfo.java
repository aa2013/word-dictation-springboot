package dictation.word.entity.lib;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    String desc = "";
    String cover;
    String creatorName;
    Integer creator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date updateTime;
    Boolean common = false;
    //使用人数
    int uses = 0;
    //单词数
    int cnt = 0;
}
