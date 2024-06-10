package cn.yuencode.flowlongplus.mapper;

import cn.yuencode.flowlongplus.entity.SysTenant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 租户信息 Mapper 接口
 * </p>
 *
 * @author jiaxiaoyu
 * @since 2024-05-31
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {

    /**
     * 查询租户选项
     */
    List<SysTenant> getOptions();
}
