package cn.yuencode.flowlongplus.mapper;

import cn.yuencode.flowlongplus.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 权限菜单 Mapper 接口
 * </p>
 *
 * @author jiaxiaoyu
 * @since 2024-05-31
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 查询角色拥有的权限
     *
     * @param roleIds 角色ID
     */
    List<SysPermission> selectListByRoleIds(List<String> roleIds);
}
