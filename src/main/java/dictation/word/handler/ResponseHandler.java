package dictation.word.handler;

import com.alibaba.fastjson.JSONObject;
import dictation.word.entity.response.ErrorResult;
import dictation.word.entity.response.ResultBean;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * 统一结果返回处理器
 *
 * @author ljh
 * @date 2021/11/15 20:10:04
 */
@ControllerAdvice(basePackages = "dictation.word")
@Slf4j
public class ResponseHandler implements ResponseBodyAdvice<Object> {
    private final String[] RESP_EXCLUDE_PATHS = new String[]{
            "/v2/api-docs",
            "/swagger",
            "/login"
    };

    private boolean isRespExcludePaths(String url) {
        for (String path : RESP_EXCLUDE_PATHS) {
            if (url.startsWith(path)) {
                return true;
            }
        }
        return false;
    }


    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        System.out.println(request.getURI().getPath());
        if (isRespExcludePaths(request.getURI().getPath())) {
            return o;
        }
        final ResultBean suc = ResultBean.suc(o);
        if (o instanceof ErrorResult) {
            //错误返回
            ErrorResult result = (ErrorResult) o;
            return ResultBean.fail(result.getStatus(), result.getMessage());
        } else if (o instanceof String) {
            //字符串构造json返回
            return JSONObject.toJSONString(suc);
        }
        return suc;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

}
