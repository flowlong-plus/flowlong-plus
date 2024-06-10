package cn.yuencode.flowlongplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.yuencode.flowlongplus.controller.req.SysRolePageParamReq;
import cn.yuencode.flowlongplus.controller.req.SysRoleReq;
import cn.yuencode.flowlongplus.service.SysPermissionService;
import cn.yuencode.flowlongplus.service.SysRoleService;
import cn.yuencode.flowlongplus.util.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 角色
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@Slf4j
@RequestMapping("/api/role")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysPermissionService sysPermissionService;

    /**
     * 获取角色分页列表
     */
    @SaCheckLogin
    @SaCheckPermission("role:list")
    @ApiOperation("获取角色分页列表")
    @GetMapping("/list")
    public R<Object> list(SysRolePageParamReq params) {
        return R.success(sysRoleService.getList(params));
    }

    /**
     * 获取角色详情
     */
    @SaCheckLogin
    @SaCheckPermission("role:info")
    @ApiOperation("获取角色详情")
    @GetMapping("/info")
    public R<Object> info(String roleId) {
        return R.success(sysRoleService.getInfo(roleId));
    }

    /**
     * 查询所有权限列表
     */
    @SaCheckLogin
    @SaCheckPermission("role:info")
    @ApiOperation("查询所有权限列表")
    @GetMapping("/listAllPermission")
    public R<Object> listAllPermission() {
        return R.success(sysPermissionService.getPermissionTree());
    }

    /**
     * 新增角色
     */
    @SaCheckLogin
    @SaCheckPermission("role:add")
    @ApiOperation("新增角色")
    @PostMapping("/add")
    public R<Object> addRole(@RequestBody SysRoleReq roleReq) {
        sysRoleService.addRole(roleReq);
        return R.success();
    }

    /**
     * 修改角色
     */
    @SaCheckLogin
    @SaCheckPermission("role:update")
    @ApiOperation("修改角色")
    @PostMapping("/update")
    public R<Object> updateRole(@RequestBody SysRoleReq roleReq) {
        sysRoleService.updateRole(roleReq);
        return R.success();
    }

    /**
     * 删除角色
     */
    @SaCheckLogin
    @SaCheckPermission("role:delete")
    @ApiOperation("删除角色")
    @DeleteMapping("/delete")
    public R<Object> deleteRole(@RequestParam String[] ids) {
        sysRoleService.deleteRole(ids);
        return R.success();
    }

    /**
     * 获取选项列表
     */
    @SaCheckLogin
    @SaCheckPermission("role:options")
    @ApiOperation("获取选项列表")
    @GetMapping("/options")
    public R<Object> options() {
        return R.success(sysRoleService.options());
    }

}
