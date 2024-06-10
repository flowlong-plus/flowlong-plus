package cn.yuencode.flowlongplus.config.mybatis;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程变量存储
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Component
public class ApiContext {

    private static final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_TENANT_ID";
    private static final String KEY_CURRENT_TENANT_KEY = "KEY_CURRENT_TENANT_KEY";

    private static final Map<String, Object> CONTEXT = new ConcurrentHashMap<>();

    public String getCurrentTenantId() {
        return (String) CONTEXT.get(KEY_CURRENT_TENANT_ID);
    }

    public void setCurrentTenantId(String tenantId) {
        CONTEXT.put(KEY_CURRENT_TENANT_ID, tenantId);
    }

    public String getCurrentTenantKey() {
        return (String) CONTEXT.get(KEY_CURRENT_TENANT_KEY);
    }

    public void setCurrentTenantKey(String tenantId) {
        CONTEXT.put(KEY_CURRENT_TENANT_KEY, tenantId);
    }


}

