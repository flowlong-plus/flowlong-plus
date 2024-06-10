package cn.yuencode.flowlongplus.config.system;

import cn.hutool.core.util.StrUtil;
import cn.yuencode.flowlongplus.config.mybatis.ApiContext;
import cn.yuencode.flowlongplus.config.redis.RedisService;
import cn.yuencode.flowlongplus.entity.SysTenant;
import cn.yuencode.flowlongplus.mapper.SysTenantMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * WebMvc配置
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Configuration
public class MVCConfig implements WebMvcConfigurer {
    @Resource
    private RedisService redisService;
    @Resource
    private ApiContext apiContext;
    @Resource
    private SysTenantMapper sysTenantMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(setTenantIdInterceptor())
                .addPathPatterns("/**").order(0);
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        //允许所有域名进行跨域调用
        config.addAllowedOriginPattern(CorsConfiguration.ALL);
        //允许跨越发送cookie
        config.setAllowCredentials(true);
        //放行全部原始头信息
        config.addAllowedHeader(CorsConfiguration.ALL);
        //允许所有请求方法跨域调用
        config.addAllowedMethod(CorsConfiguration.ALL);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public HandlerInterceptor setTenantIdInterceptor() {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                // 租户id
                String tenantKey = request.getHeader("tenantId");

                if (StrUtil.isNotEmpty(tenantKey)) {
                    apiContext.setCurrentTenantKey(tenantKey);
                    String key = "tenant_id" + ":" + tenantKey;
                    String redisTenantId = redisService.get(key);
                    // 在上下文中设置当前多租户id
                    if (StrUtil.isNotEmpty(redisTenantId)) {
                        apiContext.setCurrentTenantId(redisTenantId);
                        // 过期租户过滤
                        SysTenant sysTenant = sysTenantMapper.selectOne(Wrappers.<SysTenant>lambdaQuery()
                                .eq(SysTenant::getId, redisTenantId)
                                .eq(SysTenant::getStatus, "1")
                                .le(SysTenant::getBeginDate, LocalDateTime.now())
                                .gt(SysTenant::getEndDate, LocalDateTime.now()));
                        if (sysTenant != null) {
                            apiContext.setCurrentTenantId(sysTenant.getTenantId());
                        } else {
                            apiContext.setCurrentTenantId(null);
                        }
                    }
                }
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }
        };
    }
}
