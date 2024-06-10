package cn.yuencode.flowlongplus.config.satoken;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * sa-token配置类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Configuration
public class SaTokenConfiguration implements WebMvcConfigurer {
    private static final String TOKEN_NAME = "token";
    private static final Integer TIMEOUT = 2592000;
    private static final Integer ACTIVITY_TIMEOUT = -1;
    private static final String TOKEN_STYLE = "uuid";

    @Bean
    public SaTokenConfig saTokenConfig() {
        SaTokenConfig saTokenConfig = new SaTokenConfig();
        // token名称 (同时也是cookie名称)
        saTokenConfig.setTokenName(TOKEN_NAME);
        // token有效期，单位s 默认30天, -1代表永不过期
        saTokenConfig.setTimeout(TIMEOUT);
        // token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒
        saTokenConfig.setActiveTimeout(ACTIVITY_TIMEOUT);
        // 是否允许同一账号并发登录
        saTokenConfig.setIsConcurrent(true);
        // 在多人登录同一账号时，是否共用一个token
        saTokenConfig.setIsShare(true);
        // token风格
        saTokenConfig.setTokenStyle(TOKEN_STYLE);
        return saTokenConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa拦截器
        registry.addInterceptor(new SaInterceptor((handle) -> {
        })).addPathPatterns("/**").order(1);
    }
}
