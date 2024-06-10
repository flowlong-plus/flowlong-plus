package cn.yuencode.flowlongplus.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 重写TenantLineInnerInterceptor#beforeQuery,过滤不需要实现租户隔离的mapper方法
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MyTenantLineInnerInterceptor extends TenantLineInnerInterceptor {

    private static List<String> IGNORE_STATEMENT_NAMES = new ArrayList<>();

    static {
        IGNORE_STATEMENT_NAMES.add("cn.yuencode.flowlongplus.mapper.SysTenantMapper.getOptions");
    }

    public MyTenantLineInnerInterceptor() {
        super();
    }

    public MyTenantLineInnerInterceptor(TenantLineHandler tenantLineHandler) {
        super(tenantLineHandler);
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (IGNORE_STATEMENT_NAMES.contains(ms.getId())) {
            return;
        }
        super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }
}
