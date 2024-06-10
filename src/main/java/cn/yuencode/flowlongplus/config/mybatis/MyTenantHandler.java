package cn.yuencode.flowlongplus.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现TenantLineHandler，过滤不需要实现租户隔离的表
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Component
@Slf4j
public class MyTenantHandler implements TenantLineHandler {

    /**
     * 多租户标识
     */
    private static final String SYSTEM_TENANT_ID = "tenant_id";
    /**
     * 需要过滤的表
     */
    private static final List<String> IGNORE_TENANT_TABLES = new ArrayList<>();

    static {
//        IGNORE_TENANT_TABLES.add("sys_tenant");
        IGNORE_TENANT_TABLES.add("sys_storage_config");
    }

    @Resource
    private ApiContext apiContext;

    /**
     * 租户Id
     *
     * @return Expression
     */
    @Override
    public Expression getTenantId() {
        String tenantId = apiContext.getCurrentTenantId();
        log.debug("当前租户为{}", tenantId);
        if (tenantId == null) {
            return new NullValue();
        }
        return new StringValue(tenantId);
    }

    /**
     * 租户字段名
     *
     * @return String
     */
    @Override
    public String getTenantIdColumn() {
        return SYSTEM_TENANT_ID;
    }

    /**
     * 根据表名判断是否进行过滤
     *
     * @param tableName 表名称
     * @return boolean
     */
    @Override
    public boolean ignoreTable(String tableName) {
        boolean ignore = IGNORE_TENANT_TABLES.stream().anyMatch((e) -> e.equalsIgnoreCase(tableName));
        if (ignore) {
            log.info("忽略租户隔离:{}", tableName);
            return true;
        }
        return false;
    }

}
