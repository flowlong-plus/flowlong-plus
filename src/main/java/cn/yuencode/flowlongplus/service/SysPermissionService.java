package cn.yuencode.flowlongplus.service;

import cn.yuencode.flowlongplus.dto.PermissionTreeItemDto;
import cn.yuencode.flowlongplus.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 权限菜单 服务类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 查询权限树 增加了label、key
     */
    Object listAllPermission();

    /**
     * 获取权限树
     */
    List<PermissionTreeItemDto> getPermissionTree();

    /**
     * 添加权限
     *
     * @param permission 权限数据
     */
    void addPermission(SysPermission permission);

    /**
     * 更新权限
     *
     * @param permission 权限数据
     */
    void updatePermission(SysPermission permission);

    /**
     * 删除权限
     *
     * @param ids 权限id
     */
    void deletePermission(String[] ids);

    /**
     * 查询权限信息
     *
     * @param permissionId 权限id
     * @return 权限信息
     */
    Object getPermission(String permissionId);
}
