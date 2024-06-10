package cn.yuencode.flowlongplus.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjUtil;
import cn.yuencode.flowlongplus.assist.Assert;
import cn.yuencode.flowlongplus.config.mybatis.ApiContext;
import cn.yuencode.flowlongplus.config.redis.RedisService;
import cn.yuencode.flowlongplus.controller.req.LoginReq;
import cn.yuencode.flowlongplus.dto.PermissionTreeItemDto;
import cn.yuencode.flowlongplus.dto.RouterMenu;
import cn.yuencode.flowlongplus.dto.RouterMeta;
import cn.yuencode.flowlongplus.entity.SysUser;
import cn.yuencode.flowlongplus.mapper.SysUserMapper;
import cn.yuencode.flowlongplus.service.AuthService;
import cn.yuencode.flowlongplus.service.SysPermissionService;
import cn.yuencode.flowlongplus.util.MD5Util;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录授权 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Resource
    private RedisService redisService;
    @Resource
    private ApiContext apiContext;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysPermissionService sysPermissionService;

    /**
     * 生成TenantId
     *
     * @return 随机UUID
     */
    private static String generateTenantId() {
        String tenantId = UUID.randomUUID(true).toString();
        return tenantId.replaceAll("-", "");
    }

    @Override
    public Object authLogin(LoginReq params) {
        Assert.isEmpty(params.getVerCode(), "验证码不能为空");
        Assert.isEmpty(params.getTenantId(), "未知租户ID");

        String captchaCode = params.getVerCode();
        String key = "captcha_" + captchaCode.toUpperCase();
        Assert.isNull(redisService.get(key), "验证码错误");
        apiContext.setCurrentTenantId(params.getTenantId());

        SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getAccount, params.getAccount()));
        Assert.isNull(sysUser, "账号或密码错误");
        Assert.isFalse(sysUser.getIsEnable(), "账号已被禁用");
        Assert.isTrue(sysUser.getPassword().equals(MD5Util.getPassword(params.getPassword())),
                "账号或密码错误");
        // sa-token登录
        StpUtil.login(sysUser.getId());
        String saToken = StpUtil.getTokenValue();
        String keyTenantId = generateTenantId();
        redisService.setString("tenant_id" + ":" + keyTenantId,
                params.getTenantId());
        redisService.expire("tenant_id" + ":" + keyTenantId,
                2592000, TimeUnit.SECONDS);
        Map<String, Object> resp = new HashMap<>();
        resp.put("tenantId", keyTenantId);
        resp.put("token", saToken);
        return resp;
    }

    @Override
    public void logout() {
        String tenantKey = "tenant_id" + ":" + apiContext.getCurrentTenantKey();
        redisService.delete(tenantKey);
        StpUtil.logout();
    }

    @Override
    public Object getRouters() {
        List<PermissionTreeItemDto> permissionTree = sysPermissionService.getPermissionTree();
        Map<String, Object> resp = new HashMap<>();
        resp.put("routes", buildRouterMenus(permissionTree));
        resp.put("home", "/home");
        return resp;
    }

    @Override
    public Object getInfo() {
        String loginId = (String) StpUtil.getLoginId();
        SysUser sysUser = sysUserMapper.selectById(loginId);
        Assert.isNull(sysUser, "未知用户");
        Assert.isFalse(sysUser.getIsEnable(), "账号已被禁用,请联系管理员");
        Map<String, Object> resp = new HashMap<>();
        resp.put("userId", sysUser.getId());
        resp.put("account", sysUser.getAccount());
        resp.put("nickname", sysUser.getNickname());
        resp.put("email", sysUser.getEmail());
        resp.put("mobile", sysUser.getMobile());
        resp.put("sex", sysUser.getSex());
        return resp;
    }

    /**
     * 构建前端路由菜单树
     */
    private List<RouterMenu> buildRouterMenus(List<PermissionTreeItemDto> list) {
        List<RouterMenu> routers = new LinkedList<>();
        for (PermissionTreeItemDto item : list) {
            RouterMenu router = RouterMenu.builder()
                    .name(item.getRouteName())
                    .path(item.getRoutePath())
                    .component(item.getComponent())
                    .redirect("")
                    .build();

            RouterMeta meta = RouterMeta.builder()
                    .title(item.getTitle())
                    .i18nKey(item.getI18nKey())
                    .keepAlive(item.getKeepAlive())
                    .constant(item.getConstant())
                    .icon(item.getIcon())
                    .localIcon(item.getLocalIcon())
                    .order(item.getOrderNum())
                    .openType(item.getOpenType())
                    .hideInMenu(item.getHideInMenu())
                    .multiTab(item.getMultiTab())
                    .fixedIndexInTab(item.getFixedIndexInTab())
                    .build();
            router.setMeta(meta);

            // 递归调用
            List<PermissionTreeItemDto> children = item.getChildren();
            // 0=目录
            if (ObjUtil.isNotEmpty(children) && "0".equals(item.getMenuType())) {
                router.setChildren(buildRouterMenus(children));
            }
            routers.add(router);
        }

        return routers;
    }
}
