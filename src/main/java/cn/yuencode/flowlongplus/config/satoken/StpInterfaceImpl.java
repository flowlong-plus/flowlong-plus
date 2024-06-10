package cn.yuencode.flowlongplus.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.StrUtil;
import cn.yuencode.flowlongplus.entity.SysPermission;
import cn.yuencode.flowlongplus.entity.SysRole;
import cn.yuencode.flowlongplus.entity.SysRolePermission;
import cn.yuencode.flowlongplus.entity.SysUserRole;
import cn.yuencode.flowlongplus.mapper.SysPermissionMapper;
import cn.yuencode.flowlongplus.mapper.SysRoleMapper;
import cn.yuencode.flowlongplus.mapper.SysRolePermissionMapper;
import cn.yuencode.flowlongplus.mapper.SysUserRoleMapper;
import cn.yuencode.flowlongplus.util.enums.RoleEnum;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * StpInterface实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {
        // 存取权限编码集合
        List<String> list = new ArrayList<>();
        // 查询用户角色
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getUserId, loginId.toString()));
        if (sysUserRoleList.size() > 0) {
            List<String> roleIds = new ArrayList<>();
            for (SysUserRole sysUserRole : sysUserRoleList) {
                roleIds.add(sysUserRole.getRoleId());
            }
            // 查询角色权限
            boolean isSuperAdmin = false;
            List<SysRole> roleList = sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
                    .in(SysRole::getId, roleIds));
            if (null != roleList && roleList.size() > 0) {
                for (SysRole sysRole : roleList) {
                    if (sysRole.getRoleCode().equals(RoleEnum.AUTH.getCode())) {
                        isSuperAdmin = true;
                        break;
                    }
                }
            }

            if (isSuperAdmin) { // 超级管理员有所有权限
                list.add("*");
            } else {
                List<SysRolePermission> sysRolePermissionList = sysRolePermissionMapper
                        .selectList(Wrappers.<SysRolePermission>lambdaQuery().in(SysRolePermission::getRoleId, roleIds));

                List<String> permissionIds = new ArrayList<>();
                for (SysRolePermission sysRolePermission : sysRolePermissionList) {
                    permissionIds.add(sysRolePermission.getPermissionId());
                }

                List<SysPermission> sysPermissionList = sysPermissionMapper
                        .selectList(Wrappers.<SysPermission>lambdaQuery().in(SysPermission::getId, permissionIds));

                for (SysPermission sysPermission : sysPermissionList) {
                    if (null != sysPermission.getPermissionCode() && !"".equals(sysPermission.getPermissionCode())) {
                        list.add(sysPermission.getPermissionCode());
                    }
                }
            }

        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        log.info("当前用户:{} 拥有权限:{}", loginId, jsonObject);
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        List<String> list = new ArrayList<>();
        // 查询用户角色
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getUserId, loginId.toString()));

        List<String> roleIds = new ArrayList<>();
        for (SysUserRole sysUserRole : sysUserRoleList) {
            roleIds.add(sysUserRole.getRoleId());
        }

        List<SysRole> roleList = sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery().in(SysRole::getId, roleIds));
        for (SysRole sysRole : roleList) {
            if (StrUtil.isNotEmpty(sysRole.getRoleCode())) {
                list.add(sysRole.getRoleCode());
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        log.info("当前用户:{} 所属角色:{}", loginId, jsonObject);
        return list;
    }
}
