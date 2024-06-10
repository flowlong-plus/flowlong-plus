package cn.yuencode.flowlongplus.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.yuencode.flowlongplus.assist.Assert;
import cn.yuencode.flowlongplus.dto.PermissionTreeItemDto;
import cn.yuencode.flowlongplus.entity.SysPermission;
import cn.yuencode.flowlongplus.entity.SysRole;
import cn.yuencode.flowlongplus.entity.SysRolePermission;
import cn.yuencode.flowlongplus.entity.SysUserRole;
import cn.yuencode.flowlongplus.enums.EnableStatus;
import cn.yuencode.flowlongplus.mapper.SysPermissionMapper;
import cn.yuencode.flowlongplus.mapper.SysRoleMapper;
import cn.yuencode.flowlongplus.mapper.SysRolePermissionMapper;
import cn.yuencode.flowlongplus.mapper.SysUserRoleMapper;
import cn.yuencode.flowlongplus.service.SysPermissionService;
import cn.yuencode.flowlongplus.util.enums.RoleEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限菜单 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public Object listAllPermission() {
        List<PermissionTreeItemDto> permissionTree = getPermissionTree();
        return permissionTree.stream().map(item -> {
            Map<String, Object> map = BeanUtil.beanToMap(item);
            map.put("key", item.getId());
            map.put("label", item.getMenuName());
            map.remove("children");
            return map;
        }).collect(Collectors.toList());
    }


    @Override
    public List<PermissionTreeItemDto> getPermissionTree() {
        String loginId = (String) StpUtil.getLoginId();
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getUserId, loginId));
        // 是否为超级管理员
        boolean isSuperAdmin = userRoles.stream().anyMatch(userRole -> {
            SysRole sysRole = sysRoleMapper.selectById(userRole.getRoleId());
            return sysRole.getRoleCode().equals(RoleEnum.AUTH.getCode());
        });
        List<SysPermission> sysPermissions;
        if (isSuperAdmin) {
            sysPermissions = sysPermissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
                    .in(SysPermission::getMenuType, 0, 1)
                    .eq(SysPermission::getStatus, EnableStatus.enable.getValue())
                    .orderByAsc(SysPermission::getParentId)
                    .orderByAsc(SysPermission::getOrderNum));
        } else {
            List<String> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            sysPermissions = sysPermissionMapper.selectListByRoleIds(roleIds);
        }
        return getChildPerms(sysPermissions, "0");
    }

    @Override
    public void addPermission(SysPermission permission) {
        permission.setId(null);
        if (ObjUtil.isNotEmpty(permission.getParentId()) && !permission.getParentId().equals("0")) {
            SysPermission parentPermission = sysPermissionMapper.selectById(permission.getParentId());
            Assert.isNull(parentPermission);
            permission.setLevel(parentPermission.getLevel() + 1);
        } else {
            permission.setLevel(1);
            permission.setParentId("0");
        }
        permission.setTitle(permission.getMenuName());
        baseMapper.insert(permission);
    }

    @Override
    public void updatePermission(SysPermission permission) {
        SysPermission sysPermission = baseMapper.selectById(permission.getId());
        Assert.isNull(sysPermission, "未知权限");
        if (ObjUtil.isNotEmpty(permission.getParentId()) && !permission.getParentId().equals("0")) {
            SysPermission parentPermission = sysPermissionMapper.selectById(permission.getParentId());
            Assert.isNull(parentPermission);
            permission.setLevel(parentPermission.getLevel() + 1);
        } else {
            permission.setLevel(1);
            permission.setParentId("0");
        }
        permission.setTitle(permission.getMenuName());
        baseMapper.updateById(permission);
    }

    @Override
    public void deletePermission(String[] ids) {
        for (String id : ids) {
            Long l = sysRolePermissionMapper.selectCount(Wrappers.<SysRolePermission>lambdaQuery()
                    .eq(SysRolePermission::getPermissionId, id));
            if (l > 0) {
                SysPermission sysPermission = sysPermissionMapper.selectById(id);
                throw Assert.throwable("该权限 [" + sysPermission.getMenuName() + "] 已被分配，无法删除");
            }
        }
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public Object getPermission(String permissionId) {
        SysPermission sysPermission = baseMapper.selectById(permissionId);
        Assert.isNull(sysPermission, "未知权限");
        return sysPermission;
    }

    /**
     * 构建权限菜单树
     */
    private List<PermissionTreeItemDto> getChildPerms(List<SysPermission> sysPermissions, String parentId) {
        List<PermissionTreeItemDto> list = sysPermissions.stream()
                .map(sysPermission -> BeanUtil.copyProperties(sysPermission, PermissionTreeItemDto.class))
                .collect(Collectors.toList());
        List<PermissionTreeItemDto> returnList = new ArrayList<>();
        for (PermissionTreeItemDto t : list) {
            if (t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<PermissionTreeItemDto> list, PermissionTreeItemDto t) {
        // 得到子节点列表
        List<PermissionTreeItemDto> childList = getChildList(list, t);
        t.setChildren(childList);
        for (PermissionTreeItemDto tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 获取子节点
     */
    private List<PermissionTreeItemDto> getChildList(List<PermissionTreeItemDto> list, PermissionTreeItemDto t) {
        List<PermissionTreeItemDto> tlist = new ArrayList<>();
        for (PermissionTreeItemDto n : list) {
            if (n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<PermissionTreeItemDto> list, PermissionTreeItemDto t) {
        return !getChildList(list, t).isEmpty();
    }

}
