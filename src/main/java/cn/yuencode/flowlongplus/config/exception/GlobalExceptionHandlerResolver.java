package cn.yuencode.flowlongplus.config.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.yuencode.flowlongplus.util.R;
import cn.yuencode.flowlongplus.util.enums.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常拦截
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerResolver {
    /**
     * GET/POST请求方法错误的拦截器
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Object> httpRequestMethodHandler(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("请求地址'{}', 异常信息:'{}'", request.getRequestURI(), e.getMessage());
        return R.fail(ErrorEnum.E_500);
    }

    /**
     * 租户id错误
     */
    @ExceptionHandler(TenantIdException.class)
    public R<Object> tenantIdExceptionHandler(TenantIdException e, HttpServletRequest request) {
        log.error("请求地址'{}', 异常信息:'{}'", request.getRequestURI(), e.getMessage());
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(NotLoginException.class)
    public R<Object> notLoginExceptionHandler(NotLoginException e, HttpServletRequest request) {
        // 验证sa-token 未登录判断
        log.error("请求地址'{}', 异常信息:'{}'", request.getRequestURI(), e.getMessage());
        return R.fail(ErrorEnum.E_20001);
    }

    @ExceptionHandler(NotPermissionException.class)
    public R<Object> notPermissionExceptionHandler(NotPermissionException e, HttpServletRequest request) {
        // cn.dev33.satoken.exception.NotPermissionException: 无此权限：user:add
        log.error("请求地址'{}', 异常信息:'{}'", request.getRequestURI(), e.getMessage());
        return R.fail(ErrorEnum.E_502);

    }

    @ExceptionHandler(CommonException.class)
    public R<Object> commonExceptionHandler(CommonException e, HttpServletRequest request) {
        // 业务异常
        log.error("请求地址'{}', 异常信息:'{}'", request.getRequestURI(), e.getMessage());
        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            return R.fail(e.getMessage());
        }
        return R.fail(ErrorEnum.E_500);
    }


    /**
     * 全局异常拦截
     */
    @ExceptionHandler(Exception.class)
    public R<Object> exceptionHandler(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}', 异常信息:'{}'", request.getRequestURI(), e.getMessage());
        e.printStackTrace();
        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            return R.fail(e.getMessage());
        }
        return R.fail(ErrorEnum.E_500);
    }

}
