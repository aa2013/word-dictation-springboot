package dictation.word.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author xiongyu
 * @date 2021/8/1 23:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultBean {
    /**
     * status状态值：代表本次请求response状态结果
     */
    private Integer status;
    /**
     * response描述：对本次状态码的描述
     */
    private String desc;
    /**
     * data数据：本次返回的数据
     */
    private Object data;

    public static ResultBean suc() {
        ResultBean resultBean = new ResultBean();
        resultBean.setResultCode(ResultCode.SUCCESS);
        return resultBean;
    }

    public static ResultBean suc(Object data) {
        ResultBean resultBean = new ResultBean();
        resultBean.setResultCode(ResultCode.SUCCESS);
        resultBean.setData(data);
        return resultBean;
    }

    public static ResultBean fail(Integer status, String desc) {
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(status);
        resultBean.setDesc(desc);
        return resultBean;
    }

    public static ResultBean fail(ResultCode resultCode) {
        ResultBean resultBean = new ResultBean();
        resultBean.setResultCode(resultCode);
        return resultBean;
    }

    public static ResultBean fail(ResultCode resultCode, Exception e) {
        ResultBean resultBean = new ResultBean();
        resultBean.setResultCode(resultCode);
        resultBean.setDesc(e.getMessage());
        return resultBean;
    }


    private void setResultCode(ResultCode code) {
        this.status = code.getCode();
        this.desc = code.getMsg();
    }


}
