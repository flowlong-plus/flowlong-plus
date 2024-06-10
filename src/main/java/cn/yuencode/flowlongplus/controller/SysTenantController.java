package cn.yuencode.flowlongplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.yuencode.flowlongplus.controller.req.PageSysTenantReq;
import cn.yuencode.flowlongplus.controller.req.SysTenantReq;
import cn.yuencode.flowlongplus.entity.SysTenant;
import cn.yuencode.flowlongplus.service.SysTenantService;
import cn.yuencode.flowlongplus.util.R;
import cn.yuencode.flowlongplus.util.page.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 租户管理
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@RequestMapping("/api/tenant")
@Api(tags = "租户管理")
public class SysTenantController {
    @Resource
    private SysTenantService sysTenantService;


    /**
     * 查询租户分页列表
     */
    @SaCheckLogin
    @SaCheckPermission("tenant:list")
    @ApiOperation("查询租户分页列表")
    @GetMapping("/list")
    public R<?> list(PageSysTenantReq pageSysTenantReq) {
        PageResult<SysTenant> page = sysTenantService.getList(pageSysTenantReq);
        return R.success(page);
    }


    /**
     * 新增
     */
    @SaCheckLogin
    @SaCheckPermission("tenant:add")
    @ApiOperation("新增租户")
    @PostMapping("/add")
    public R<?> add(@RequestBody SysTenantReq sysTenantReq) {
        sysTenantService.addTenant(sysTenantReq);
        return R.success();
    }


    /**
     * 更新
     */
    @SaCheckLogin
    @SaCheckPermission("tenant:update")
    @ApiOperation("更新租户")
    @PostMapping("/update")
    public R<?> update(@RequestBody SysTenantReq systenantReq) {
        sysTenantService.updateTenant(systenantReq);
        return R.success();

    }


    /**
     * 删除
     */
    @SaCheckLogin
    @SaCheckPermission("tenant:delete")
    @ApiOperation("删除租户")
    @DeleteMapping("/delete")
    public R<?> delete(@RequestParam String[] ids) {
        sysTenantService.deleteIds(ids);
        return R.success();
    }


    /**
     * 获取单个租户
     */
    @SaCheckLogin
    @SaCheckPermission("tenant:info")
    @ApiOperation("获取租户信息")
    @GetMapping("/info")
    public R<?> info(String tenantId) {
        return R.success(sysTenantService.getInfo(tenantId));
    }

    /**
     * 获取租户选项列表
     */
    @ApiOperation("获取租户选项列表")
    @GetMapping("/options")
    public R<Object> options() {
        return R.success(sysTenantService.options());
    }


}
