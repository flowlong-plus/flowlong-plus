package cn.yuencode.flowlongplus.workflow.controller;

import cn.yuencode.flowlongplus.util.R;
import cn.yuencode.flowlongplus.workflow.service.OaService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 组织和部门
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@RequestMapping("/workflow/oa")
@Api(tags = {"组织部门接口"})
public class OaController {

    @Resource
    private OaService oaService;

    /**
     * 查询组织架构树
     */
    @GetMapping("/org/tree")
    public R<?> getOrgTreeData(@RequestParam(defaultValue = "0") Integer deptId) {
        return R.success(oaService.getOrgTreeData(deptId));
    }

    /**
     * 模糊搜索用户
     */
    @GetMapping("/org/tree/user/search")
    public R<?> getOrgTreeUser(@RequestParam String username) {
        return R.success(oaService.getOrgTreeUser(username.trim()));
    }

    /**
     * 查询用户信息
     */
    @GetMapping("/org/getUserInfo")
    public R<?> getUserInfo(@RequestParam("userId") String userId) {
        return R.success(oaService.getUserInfo(userId));
    }

}
