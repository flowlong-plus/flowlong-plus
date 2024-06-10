package cn.yuencode.flowlongplus.workflow.controller;

import cn.yuencode.flowlongplus.util.R;
import cn.yuencode.flowlongplus.workflow.controller.req.*;
import cn.yuencode.flowlongplus.workflow.service.WorkspaceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 流程工作台相关接口
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@RequestMapping("/workflow/workspace")
public class WorkspaceController {
    @Resource
    private WorkspaceService workspaceService;

    /**
     * 启动流程
     */
    @PostMapping("/process/start")
    public R<?> startProcess(@RequestBody StartProcessReq startProcessReq) {
        workspaceService.startProcess(startProcessReq);
        return R.success();
    }

    /**
     * 查询流程信息
     */
    @GetMapping("/process/detail")
    public R<?> processDetail(@RequestParam String templateId) {
        return R.success(workspaceService.processDetail(templateId));
    }

    /**
     * 流程过程预览
     */
    @PostMapping("/process/preview")
    public R<?> processPreview(@RequestBody StartProcessReq params) {
        return R.success(workspaceService.processPreview(params));
    }

    /**
     * 待审批任务
     */
    @PostMapping("/process/todoTask")
    public R<?> getTodoTask(@RequestBody ProcessListReq queryParams) {
        return R.success(workspaceService.getTodoTask(queryParams));
    }

    /**
     * 我发起的流程
     */
    @PostMapping("/process/applyList")
    public R<?> getApplyList(@RequestBody ProcessListReq queryParams) {
        return R.success(workspaceService.getApplyList(queryParams));
    }

    /**
     * 抄送我的
     */
    @PostMapping("/process/ccList")
    public R<?> getCcList(@RequestBody ProcessListReq queryParams) {
        return R.success(workspaceService.getCcList(queryParams));
    }

    /**
     * 已审批列表
     */
    @PostMapping("/process/doneList")
    public R<?> getDoneList(@RequestBody ProcessListReq queryParams) {
        return R.success(workspaceService.getDoneList(queryParams));
    }

    /**
     * 查询实例表单数据
     */
    @GetMapping("/process/instanceForm")
    public R<?> getInstanceForm(@RequestParam String instanceId) {
        return R.success(workspaceService.getInstanceForm(instanceId));
    }

    /**
     * 查询实例动作记录
     */
    @GetMapping("/process/instanceAction")
    public R<?> getInstanceAction(@RequestParam String instanceId) {
        return R.success(workspaceService.getInstanceAction(instanceId));
    }

    /**
     * 查询当前节点信息
     */
    @GetMapping("/process/getNodeInfo")
    public R<?> getNodeInfo(@RequestParam String processId, @RequestParam String nodeId) {
        return R.success(workspaceService.getNodeInfo(processId, nodeId));
    }

    /**
     * 同意
     */
    @PostMapping("/agree")
    public R<?> agree(@RequestBody ProcessAgreeReq processAgreeReq) {
        workspaceService.agree(processAgreeReq);
        return R.success();
    }

    /**
     * 拒绝
     */
    @PostMapping("/refuse")
    public R<?> refuse(@RequestBody ProcessRefuseReq processRefuseReq) {
        workspaceService.refuse(processRefuseReq);
        return R.success();
    }

    /**
     * 撤销流程
     */
    @PostMapping("/revoke")
    public R<?> revoke(@RequestBody ProcessRevokeReq processRevokeReq) {
        workspaceService.revoke(processRevokeReq);
        return R.success();
    }

    /**
     * 评论
     */
    @PostMapping("/comment")
    public R<?> comment(@RequestBody ProcessCommentReq params) {
        workspaceService.comment(params);
        return R.success();
    }
}
