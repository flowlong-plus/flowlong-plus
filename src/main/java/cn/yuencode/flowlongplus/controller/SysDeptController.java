package cn.yuencode.flowlongplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.yuencode.flowlongplus.controller.req.SysDeptPageParamReq;
import cn.yuencode.flowlongplus.entity.SysDept;
import cn.yuencode.flowlongplus.service.SysDeptService;
import cn.yuencode.flowlongplus.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 部门
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@RequestMapping("/api/dept")
public class SysDeptController {
    @Resource
    private SysDeptService sysDeptService;

    /**
     * 查询部门分页列表(未使用)
     */
    @SaCheckLogin
    @SaCheckPermission("dept:list")
    @ApiOperation("查询部门分页列表(未使用)")
    @GetMapping("/list")
    public R<Object> list(SysDeptPageParamReq params) {
        return R.success(sysDeptService.getList(params));
    }

    /**
     * 获取部门树
     */
    @SaCheckLogin
    @ApiOperation("获取部门树")
    @GetMapping("/getDeptTree")
    public R<Object> getDeptTree() {
        return R.success(sysDeptService.getDeptTree());
    }

    /**
     * 新增
     */
    @SaCheckLogin
    @SaCheckPermission("dept:add")
    @ApiOperation("新增部门")
    @PostMapping("/add")
    public R<Object> addDept(@RequestBody SysDept sysDept) {
        sysDeptService.addDept(sysDept);
        return R.success();
    }

    /**
     * 更新部门
     */
    @SaCheckLogin
    @SaCheckPermission("dept:update")
    @ApiOperation("更新部门")
    @PostMapping("/update")
    public R<Object> updateDept(@RequestBody SysDept sysDept) {
        sysDeptService.updateDept(sysDept);
        return R.success();
    }

    /**
     * 删除部门
     */
    @SaCheckLogin
    @SaCheckPermission("dept:delete")
    @ApiOperation("删除部门")
    @DeleteMapping("/delete")
    public R<Object> deleteDept(@RequestParam String[] ids) {
        sysDeptService.deleteDept(ids);
        return R.success();
    }

    /**
     * 查询部门信息
     */
    @SaCheckLogin
    @SaCheckPermission("dept:info")
    @ApiOperation("查询部门信息")
    @GetMapping("/info")
    public R<Object> getDeptInfo(@RequestParam String deptId) {
        return R.success(sysDeptService.getDeptInfo(deptId));
    }
}
