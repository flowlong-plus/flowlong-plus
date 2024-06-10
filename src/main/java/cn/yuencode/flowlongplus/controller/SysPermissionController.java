package cn.yuencode.flowlongplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.yuencode.flowlongplus.entity.SysPermission;
import cn.yuencode.flowlongplus.service.SysPermissionService;
import cn.yuencode.flowlongplus.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 权限菜单
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@RequestMapping("/api/permission")
public class SysPermissionController {
    @Resource
    private SysPermissionService sysPermissionService;

    /**
     * 添加权限
     */
    @SaCheckLogin
    @SaCheckPermission("permission:add")
    @ApiOperation("添加权限")
    @PostMapping("/add")
    public R<Object> addPermission(@RequestBody SysPermission permission) {
        sysPermissionService.addPermission(permission);
        return R.success();
    }

    /**
     * 更新权限
     */
    @SaCheckLogin
    @SaCheckPermission("permission:update")
    @ApiOperation("更新权限")
    @PostMapping("/update")
    public R<Object> updatePermission(@RequestBody SysPermission permission) {
        sysPermissionService.updatePermission(permission);
        return R.success();
    }

    /**
     * 删除权限
     */
    @SaCheckLogin
    @SaCheckPermission("permission:delete")
    @ApiOperation("删除权限")
    @DeleteMapping("/delete")
    public R<Object> deletePermission(@RequestParam String[] ids) {
        sysPermissionService.deletePermission(ids);
        return R.success();
    }

    /**
     * 查询权限信息
     */
    @SaCheckLogin
    @SaCheckPermission("permission:info")
    @ApiOperation("查询权限信息")
    @GetMapping("/info")
    public R<Object> getPermission(@RequestParam String permissionId) {
        return R.success(sysPermissionService.getPermission(permissionId));
    }
}
