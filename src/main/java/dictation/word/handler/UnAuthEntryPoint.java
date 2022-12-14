package dictation.word.handler;

import cn.hutool.json.JSONUtil;
import dictation.word.entity.response.ResultBean;
import dictation.word.entity.response.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author ljh
 * 认证失败处理
 */
public class UnAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        System.out.println(e.getMessage());
        outputStream.write(JSONUtil.toJsonStr(ResultBean.fail(ResultCode.NO_AUTH)).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
