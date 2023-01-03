package dictation.word.entity.response;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xiongyu
 * @date 2021/8/1 23:27
 */
public enum ResultCode {

    /**
     * 数据操作错误定义
     */
    SUCCESS(200, "OK"),
    OTHER_ERROR(1000, "未知错误"),
    NO_PERMISSION(1001, "权限不足"),
    NO_LOGIN(1002, "账号未登录"),
    LOGIN_FAILED(1003, "登录失败，用户名或密码错误"),
    TOKEN_EXPIRE(1004, "登录过期，请重新登录"),
    PARAM_MISSING(1005, "参数缺失"),
    NO_AUTH(1006, "没有权限"),
    REG_INFO_INVALID(1007, "用户注册信息不合法"),
    USER_INFO_IS_BOUND(1008, "该手机号/邮箱已绑定"),
    ACCOUNT_STATUS_ERROR(1009, "用户状态异常"),
    FORMAT_ERROR(1010, "格式异常"),
    DUPLICATE_ERROR(1011, "重复提交！"),
    CREATE_FAILED(1012, "新建失败！"),
    DEL_FAILED(1013, "删除失败！"),
    UPDATE_FAILED(1014, "更新失败！"),
    ILLEGAL_DATA(1015, "取得的数据不合法"),
    UNAVAILABLE(1016, "服务不可用，稍后再试");
    /**
     * 错误返回码
     */
    private final Integer code;

    /**
     * 错误返回消息
     */
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("desc", msg);
        return json;
    }
}
