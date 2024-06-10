package cn.yuencode.flowlongplus.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.yuencode.flowlongplus.assist.Assert;
import cn.yuencode.flowlongplus.controller.req.SysRolePageParamReq;
import cn.yuencode.flowlongplus.controller.req.SysRoleReq;
import cn.yuencode.flowlongplus.controller.res.RoleInfoRes;
import cn.yuencode.flowlongplus.entity.SysPermission;
import cn.yuencode.flowlongplus.entity.SysRole;
import cn.yuencode.flowlongplus.entity.SysRolePermission;
import cn.yuencode.flowlongplus.entity.SysUserRole;
import cn.yuencode.flowlongplus.mapper.SysPermissionMapper;
import cn.yuencode.flowlongplus.mapper.SysRoleMapper;
import cn.yuencode.flowlongplus.mapper.SysRolePermissionMapper;
import cn.yuencode.flowlongplus.mapper.SysUserRoleMapper;
import cn.yuencode.flowlongplus.service.SysRoleService;
import cn.yuencode.flowlongplus.util.enums.RoleEnum;
import cn.yuencode.flowlongplus.util.page.PageResultConvert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 角色 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public Object getList(SysRolePageParamReq params) {
        Page<SysRole> sysRolePage = baseMapper.selectPage(Page.of(params.getPageNum(), params.getPageSize()),
                Wrappers.<SysRole>lambdaQuery()
                        .like(ObjUtil.isNotEmpty(params.getRoleName()), SysRole::getRoleName, params.getRoleName())
                        .like(ObjUtil.isNotEmpty(params.getRoleCode()), SysRole::getRoleCode, params.getRoleCode())
                        .eq(ObjUtil.isNotEmpty(params.getStatus()), SysRole::getStatus, params.getStatus())
                        .orderByDesc(SysRole::getCreateTime));
        return PageResultConvert.convertMybatisPlus(sysRolePage);
    }

    @Override
    public Object getInfo(String roleId) {
        Assert.isEmpty(roleId, "非法id");
        SysRole sysRole = baseMapper.selectById(roleId);
        Assert.isNull(roleId, "非法数据");

        // 目录菜单权限id集合
        List<String> permissionIds = new ArrayList<>();
        // 按钮权限id集合
        List<String> btnPermissionIds = new ArrayList<>();

        List<SysPermission> sysPermissions = sysPermissionMapper.selectListByRoleIds(Collections.singletonList(roleId));
        for (SysPermission sysPermission : sysPermissions) {
            // 0=目录 1=菜单 2=按钮
            if ("2".equals(sysPermission.getMenuType())) {
                btnPermissionIds.add(sysPermission.getId());
            } else {
                permissionIds.add(sysPermission.getId());
            }
        }
        return RoleInfoRes.builder()
                .id(sysRole.getId())
                .roleName(sysRole.getRoleName())
                .roleCode(sysRole.getRoleCode())
                .remark(sysRole.getRemark())
                .status(sysRole.getStatus())
                .permissions(permissionIds)
                .permissionKeys(btnPermissionIds).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(SysRoleReq roleReq) {
        baseMapper.insert(roleReq);
        // 保存权限信息
        List<String> permissions = roleReq.getPermissions();
        List<String> permissionKeys = roleReq.getPermissionKeys();
        if (ObjUtil.isNotEmpty(permissionKeys)) {
            permissions.addAll(permissionKeys);
        }
        String roleId = roleReq.getId();

        for (String permission : permissions) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setRoleId(roleId);
            sysRolePermission.setPermissionId(permission);
            sysRolePermissionMapper.insert(sysRolePermission);
        }
    }

    @Override
    public void updateRole(SysRoleReq roleReq) {
        SysRole sysRole = baseMapper.selectById(roleReq.getId());
        Assert.isNull(sysRole, "未知角色");
        Assert.isTrue(sysRole.getRoleCode().equals(RoleEnum.AUTH.code)
                        || roleReq.getRoleCode().equals(RoleEnum.AUTH.code),
                "超级管理员不允许修改");
        baseMapper.updateById(roleReq);
        // 更新权限信息
        sysRolePermissionMapper.delete(Wrappers.<SysRolePermission>lambdaQuery()
                .eq(SysRolePermission::getRoleId, sysRole.getId()));
        List<String> permissions = roleReq.getPermissions();
        List<String> permissionKeys = roleReq.getPermissionKeys();
        if (ObjUtil.isNotEmpty(permissionKeys)) {
            permissions.addAll(permissionKeys);
        }
        String roleId = roleReq.getId();

        for (String permission : permissions) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setRoleId(roleId);
            sysRolePermission.setPermissionId(permission);
            sysRolePermissionMapper.insert(sysRolePermission);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRole(String[] ids) {
        for (String id : ids) {
            SysRole sysRole = baseMapper.selectById(id);
            Assert.isNull(sysRole, "未知角色");
            Assert.isTrue(sysRole.getRoleCode().equals(RoleEnum.AUTH.code),
                    "超级管理员不允许修改");

            Long l = sysUserRoleMapper.selectCount(Wrappers.<SysUserRole>lambdaQuery()
                    .eq(SysUserRole::getRoleId, id));
            if (l > 0) {
                throw Assert.throwable("该角色 [" + sysRole.getRoleName() + "] 下存在用户，不允许删除");
            }
            baseMapper.deleteById(id);
            sysRolePermissionMapper.delete(Wrappers.<SysRolePermission>lambdaQuery()
                    .eq(SysRolePermission::getRoleId, id));
        }
    }

    @Override
    public Object options() {
        List<SysRole> sysRoles = baseMapper.selectList(Wrappers.emptyWrapper());
        List<Map<String, Object>> resp = new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            Map<String, Object> map = new HashMap<>();
            map.put("roleId", sysRole.getId());
            map.put("roleName", sysRole.getRoleName());
            map.put("roleCode", sysRole.getRoleCode());
            resp.add(map);
        }
        return resp;
    }
}
