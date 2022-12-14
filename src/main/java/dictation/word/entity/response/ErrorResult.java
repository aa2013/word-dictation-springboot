package dictation.word.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiongyu
 * @date 2021/8/1 23:27
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResult {

    /**
     * 异常状态码
     */
    private Integer status;
    /**
     * 用户看得见的异常
     */
    private String message;
    /**
     * 异常的名字
     */
    private String exceptionName;


    public static ErrorResult fail(ResultCode resultCode, Throwable e, String message) {
        ErrorResult errorResult = ErrorResult.fail(resultCode, e);
        errorResult.setMessage(message);
        return errorResult;
    }

    public static ErrorResult fail(ResultCode resultCode, Throwable e) {
        ErrorResult errorResult = new ErrorResult();
        if (resultCode == ResultCode.OTHER_ERROR) {
            errorResult.setMessage(resultCode.getMsg());
        } else {
            String msg = StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : resultCode.getMsg();
            errorResult.setMessage(msg);
        }
        errorResult.setStatus(resultCode.getCode());
        errorResult.setExceptionName(e.getClass().getName());
        return errorResult;
    }

}
