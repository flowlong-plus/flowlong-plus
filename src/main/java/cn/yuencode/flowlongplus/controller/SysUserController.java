package cn.yuencode.flowlongplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.yuencode.flowlongplus.controller.req.SysUserPageParamReq;
import cn.yuencode.flowlongplus.entity.SysUser;
import cn.yuencode.flowlongplus.service.SysUserService;
import cn.yuencode.flowlongplus.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户管理
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    /**
     * 查询用户分页列表
     */
    @SaCheckLogin
    @SaCheckPermission("user:list")
    @ApiOperation("查询用户分页列表")
    @GetMapping("/list")
    public R<Object> list(SysUserPageParamReq param) {
        return R.success(sysUserService.getList(param));
    }

    /**
     * 添加用户
     */
    @SaCheckLogin
    @SaCheckPermission("user:add")
    @ApiOperation("添加用户")
    @PostMapping("/add")
    public R<Object> add(@RequestBody SysUser sysUser) {
        sysUserService.addUser(sysUser);
        return R.success();
    }

    /**
     * 修改用户
     */
    @SaCheckLogin
    @SaCheckPermission("user:update")
    @ApiOperation("修改用户")
    @PostMapping("/update")
    public R<Object> update(@RequestBody SysUser sysUser) {
        sysUserService.updateUser(sysUser);
        return R.success();
    }

    /**
     * 删除用户
     */
    @SaCheckLogin
    @SaCheckPermission("user:del")
    @ApiOperation("删除用户")
    @DeleteMapping("/delete")
    public R<?> delUser(@RequestParam String[] ids) {
        sysUserService.deleteUser(ids);
        return R.success();
    }

    /**
     * 获取用户信息
     */
    @SaCheckLogin
    @SaCheckPermission("user:info")
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public R<Object> getInfo(String userId) {
        return R.success(sysUserService.getInfo(userId));
    }
}
