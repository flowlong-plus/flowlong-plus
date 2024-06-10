package cn.yuencode.flowlongplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.yuencode.flowlongplus.controller.req.SysPostPageParmaReq;
import cn.yuencode.flowlongplus.entity.SysPost;
import cn.yuencode.flowlongplus.service.SysPostService;
import cn.yuencode.flowlongplus.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 岗位/职务
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@RequestMapping("/api/post")
public class SysPostController {
    @Resource
    private SysPostService sysPostService;

    /**
     * 查询岗位分页列表
     */
    @SaCheckLogin
    @SaCheckPermission("post:list")
    @ApiOperation("查询岗位分页列表")
    @GetMapping("/list")

    public R<Object> list(SysPostPageParmaReq params) {
        return R.success(sysPostService.getList(params));
    }

    /**
     * 新增岗位
     */
    @SaCheckLogin
    @SaCheckPermission("post:add")
    @ApiOperation("新增岗位")
    @PostMapping("/add")

    public R<Object> add(@RequestBody SysPost sysPost) {
        sysPostService.addPost(sysPost);
        return R.success();
    }

    /**
     * 更新岗位
     */
    @SaCheckLogin
    @SaCheckPermission("post:update")
    @ApiOperation("更新岗位")
    @PostMapping("/update")

    public R<Object> update(@RequestBody SysPost sysPost) {
        sysPostService.updatePost(sysPost);
        return R.success();
    }

    /**
     * 删除岗位
     */
    @SaCheckLogin
    @SaCheckPermission("post:delete")
    @ApiOperation("删除岗位")
    @DeleteMapping("/delete")

    public R<Object> delete(@RequestParam String[] ids) {
        sysPostService.deletePost(ids);
        return R.success();
    }

    /**
     * 获取岗位信息
     */
    @SaCheckLogin
    @SaCheckPermission("post:info")
    @ApiOperation("获取岗位信息")
    @GetMapping("/info")

    public R<Object> info(String postId) {
        return R.success(sysPostService.getInfo(postId));
    }

    /**
     * 获取岗位选项
     */
    @SaCheckLogin
    @SaCheckPermission("post:options")
    @ApiOperation("获取岗位信息")
    @GetMapping("/options")

    public R<Object> options() {
        return R.success(sysPostService.options());
    }
}
