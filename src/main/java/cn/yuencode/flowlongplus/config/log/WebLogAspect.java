package cn.yuencode.flowlongplus.config.log;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


/**
 * 接口日志记录
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
    @Pointcut("within(cn.yuencode.flowlongplus..*) && " +
            "(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController))")
    public void webLog() {

    }


    /**
     * 获取IP地址
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        log.info("---------------request----------------");
        String requestUrl = request.getRequestURL().toString();
        log.info("URL : " + requestUrl);
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + getIpAddr(request));
        log.info("USER_ID : " + StpUtil.getLoginIdDefaultNull());
        Enumeration<String> enu = request.getParameterNames();
        StringBuilder str = new StringBuilder();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            str.append(name).append("=").append(request.getParameter(name));
        }
        log.info("name:" + str);

    }

    /**
     * 返回内容输出
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        log.info("---------------response----------------");
        // 处理完请求，返回内容
        try {
            log.info("返回结果 : " + JSONObject.toJSONString(ret));
        } catch (Exception e) {
            log.error("返回结果 异常" + e.getMessage(), e);
        }
    }
}
