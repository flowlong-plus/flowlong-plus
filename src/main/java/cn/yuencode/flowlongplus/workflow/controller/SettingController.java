package cn.yuencode.flowlongplus.workflow.controller;

import cn.yuencode.flowlongplus.util.R;
import cn.yuencode.flowlongplus.workflow.controller.req.ProcessTemplateReq;
import cn.yuencode.flowlongplus.workflow.controller.res.TemplateGroupRes;
import cn.yuencode.flowlongplus.workflow.service.SettingService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 流程模版设置
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@RequestMapping("/workflow")
@Api(tags = {"流程模版设置"})
public class SettingController {

    @Resource
    private SettingService settingService;

    /**
     * 查询所有模版分组
     */
    @GetMapping("/group")
    public Object getTemplateGroups() {
        return settingService.getTemplateGroups();
    }

    /**
     * 查询分组选项
     */
    @GetMapping("/group/options")
    public R<?> getGroupOptions() {
        return R.success(settingService.getGroupOptions());
    }

    /**
     * 模版分组排序
     */
    @PutMapping("/group/sort")
    public R<?> templateGroupsSort(@RequestBody List<TemplateGroupRes> groups) {
        return R.success(settingService.templateGroupsSort(groups));
    }

    /**
     * 修改分组
     */
    @PutMapping("/group")
    public Object updateTemplateGroupName(@RequestParam String id,
                                          @RequestParam String name) {
        return settingService.updateTemplateGroupName(id, name);
    }

    /**
     * 新增分组
     */
    @PostMapping("/group")
    public Object createTemplateGroup(@RequestParam String name) {
        return settingService.createTemplateGroup(name);
    }

    /**
     * 删除分组
     */
    @DeleteMapping("/group")
    public Object deleteTemplateGroup(@RequestParam Integer id) {
        return settingService.deleteTemplateGroup(id);
    }

    /**
     * 创建流程模版
     */
    @PostMapping("/template")
    public R<?> createTemplate(@RequestBody ProcessTemplateReq processTemplateReq) {
        settingService.createTemplate(processTemplateReq);
        return R.success();
    }

    /**
     * 查询流程模版信息
     */
    @GetMapping("/template/detail")
    public R<?> getTemplateDetail(@RequestParam("templateId") String templateId) {
        return R.success(settingService.getTemplateDetail(templateId));
    }

    /**
     * 更新流程模版信息
     */
    @PutMapping("/template/update")
    public R<?> updateTemplateDetail(@RequestBody ProcessTemplateReq processTemplateReq) {
        settingService.updateTemplate(processTemplateReq);
        return R.success();
    }
}
