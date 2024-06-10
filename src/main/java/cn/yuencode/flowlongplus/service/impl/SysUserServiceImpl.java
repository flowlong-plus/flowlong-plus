package cn.yuencode.flowlongplus.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.yuencode.flowlongplus.assist.Assert;
import cn.yuencode.flowlongplus.controller.req.SysUserPageParamReq;
import cn.yuencode.flowlongplus.entity.SysRole;
import cn.yuencode.flowlongplus.entity.SysUser;
import cn.yuencode.flowlongplus.entity.SysUserRole;
import cn.yuencode.flowlongplus.mapper.SysRoleMapper;
import cn.yuencode.flowlongplus.mapper.SysUserMapper;
import cn.yuencode.flowlongplus.mapper.SysUserRoleMapper;
import cn.yuencode.flowlongplus.service.SysUserService;
import cn.yuencode.flowlongplus.util.MD5Util;
import cn.yuencode.flowlongplus.util.enums.RoleEnum;
import cn.yuencode.flowlongplus.util.page.PageResultConvert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户信息 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public Object getList(SysUserPageParamReq param) {
        Page<SysUser> sysUserPage = baseMapper.selectPage(Page.of(param.getPageNum(), param.getPageSize()),
                Wrappers.<SysUser>lambdaQuery()
                        .like(ObjUtil.isNotEmpty(param.getAccount()), SysUser::getAccount, param.getAccount())
                        .like(ObjUtil.isNotEmpty(param.getNickname()), SysUser::getNickname, param.getNickname())
                        .like(ObjUtil.isNotEmpty(param.getMobile()), SysUser::getMobile, param.getMobile())
                        .like(ObjUtil.isNotEmpty(param.getEmail()), SysUser::getEmail, param.getEmail())
                        .orderByAsc(SysUser::getCreateTime));
        return PageResultConvert.convertMybatisPlus(sysUserPage);
    }

    @Override
    public void addUser(SysUser sysUser) {
        sysUser.setId(null);
        sysUser.setTenantId(null);
        if (ObjUtil.isNull(sysUser.getIsEnable())) {
            sysUser.setIsEnable(true);
        }
        Long count = baseMapper.selectCount(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getAccount, sysUser.getAccount()));
        Assert.isTrue(count > 0, "账号已存在");
        sysUser.setPassword(MD5Util.getPassword(sysUser.getPassword()));
        baseMapper.insert(sysUser);
    }

    @Override
    public void updateUser(SysUser sysUser) {
        sysUser.setTenantId(null);
        SysUser user = baseMapper.selectById(sysUser.getId());
        Assert.isNull(user, "用户不存在");
        if (!user.getId().equals(StpUtil.getLoginId())) {
            List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery()
                    .eq(SysUserRole::getUserId, sysUser.getId()));
            for (SysUserRole sysUserRole : sysUserRoles) {
                SysRole sysRole = sysRoleMapper.selectById(sysUserRole.getRoleId());
                if (sysRole.getRoleCode().equals(RoleEnum.AUTH.getCode())) {
                    throw Assert.throwable("超级管理员不能修改");
                }
            }
        }
        sysUser.setPassword(MD5Util.getPassword(sysUser.getPassword()));
        baseMapper.updateById(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String[] ids) {
        for (String id : ids) {
            if (id.equals(StpUtil.getLoginId())) {
                throw Assert.throwable("不能删除自己");
            }
            List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery()
                    .eq(SysUserRole::getUserId, id));
            for (SysUserRole sysUserRole : sysUserRoles) {
                SysRole sysRole = sysRoleMapper.selectById(sysUserRole.getRoleId());
                if (sysRole.getRoleCode().equals(RoleEnum.AUTH.getCode())) {
                    throw Assert.throwable("超级管理员不能修改");
                }
            }
            // 删除用户角色关联信息
            sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery()
                    .eq(SysUserRole::getUserId, id));
        }
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public Object getInfo(String userId) {
        SysUser sysUser = baseMapper.selectById(userId);
        Assert.isNull(sysUser, "未知用户");
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getUserId, userId));
        List<String> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        sysUser.setPassword("");
        Map<String, Object> resp = BeanUtil.beanToMap(sysUser);
        resp.put("roleIds", roleIds);
        return resp;
    }
}
