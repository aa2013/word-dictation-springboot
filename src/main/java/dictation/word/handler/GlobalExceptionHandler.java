package dictation.word.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import dictation.word.entity.response.ErrorResult;
import dictation.word.entity.response.ResultCode;
import dictation.word.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;

/**
 * 全局异常处理
 *
 * @author ljh
 * @date 2021/11/15 20:10:04
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 其它异常处理
     *
     * @param e exception
     * @return 错误返回
     */
    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.OK)
    public ErrorResult otherException(Exception e) {
        e.printStackTrace();
        return ErrorResult.fail(ResultCode.OTHER_ERROR, e);
    }

    /**
     * token过期，登录失效
     *
     * @param e exception
     * @return 错误返回
     */
    @ExceptionHandler({TokenExpireException.class})
    @ResponseStatus(HttpStatus.OK)
    public ErrorResult tokenExpireException(Exception e) {
        return ErrorResult.fail(ResultCode.TOKEN_EXPIRE, e, e.getMessage());
    }

    /**
     * 访问无权限处理
     *
     * @param e exception
     * @return 错误返回
     */
    @ExceptionHandler({AccessDeniedException.class, NoPermissionException.class})
    @ResponseStatus(HttpStatus.OK)
    public ErrorResult accessDeniedException(Exception e) {
        if (e instanceof TokenExpireException) {
            return ErrorResult.fail(ResultCode.TOKEN_EXPIRE, e, e.getMessage());
        }
        return ErrorResult.fail(ResultCode.NO_PERMISSION, e);
    }

    /**
     * 数据为空异常处理
     *
     * @param e exception
     * @return 错误返回
     */
    @ExceptionHandler({BindException.class, UnexpectedTypeException.class, ConstraintViolationException.class, MissingServletRequestParameterException.class, IllegalStateException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    public ErrorResult bindExceptionHandler(Exception e) {
        log.error(ExceptionUtil.getMessage(e));
        return ErrorResult.fail(ResultCode.PARAM_MISSING, e, "参数缺失");
    }

    /**
     * 重复提交异常
     *
     * @param e exception
     * @return 错误返回
     */
    @ExceptionHandler({DuplicateKeyException.class})
    @ResponseStatus(HttpStatus.OK)
    public ErrorResult duplicateKeyExceptionHandler(Exception e) {
        log.error(ExceptionUtil.getMessage(e));
        return ErrorResult.fail(ResultCode.DUPLICATE_ERROR, e, "请勿重复提交！");
    }

    /**
     * 新建异常
     *
     * @param e exception
     * @return 错误返回
     */
    @ExceptionHandler({CreateNewException.class})
    @ResponseStatus(HttpStatus.OK)
    public ErrorResult createNewExceptionHandler(Exception e) {
        log.error(ExceptionUtil.getMessage(e));
        return ErrorResult.fail(ResultCode.CREATE_FAILED, e);
    }

    /**
     * 删除异常
     *
     * @param e exception
     * @return 错误返回
     */
    @ExceptionHandler({DelException.class})
    @ResponseStatus(HttpStatus.OK)
    public ErrorResult delExceptionHandler(Exception e) {
        log.error(ExceptionUtil.getMessage(e));
        return ErrorResult.fail(ResultCode.DEL_FAILED, e);
    }
    /**
     * 更新异常
     *
     * @param e exception
     * @return 错误返回
     */
    @ExceptionHandler({UpdateException.class})
    @ResponseStatus(HttpStatus.OK)
    public ErrorResult updateExceptionHandler(Exception e) {
        log.error(ExceptionUtil.getMessage(e));
        return ErrorResult.fail(ResultCode.UPDATE_FAILED, e);
    }
    /**
     * 数据不合法异常
     *
     * @param e exception
     * @return 错误返回
     */
    @ExceptionHandler({IllegalDataException.class})
    @ResponseStatus(HttpStatus.OK)
    public ErrorResult illegalDataExceptionHandler(Exception e) {
        log.error(ExceptionUtil.getMessage(e));
        return ErrorResult.fail(ResultCode.ILLEGAL_DATA, e);
    }

}
