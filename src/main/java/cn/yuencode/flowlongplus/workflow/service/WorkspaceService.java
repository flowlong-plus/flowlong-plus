package cn.yuencode.flowlongplus.workflow.service;

import cn.yuencode.flowlongplus.workflow.controller.req.*;
import cn.yuencode.flowlongplus.workflow.entity.WflowProcessTemplates;

/**
 * 工作台 服务类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface WorkspaceService {
    /**
     * 启动流程
     *
     * @param startProcessReq 请求参数
     */
    void startProcess(StartProcessReq startProcessReq);

    /**
     * 查询流程信息
     *
     * @param templateId 流程模板id
     */
    WflowProcessTemplates processDetail(String templateId);

    /**
     * 待审批任务
     */
    Object getTodoTask(ProcessListReq pageParams);

    /**
     * 同意流程
     *
     * @param processAgreeReq 请求参数
     */
    void agree(ProcessAgreeReq processAgreeReq);

    /**
     * 我发起的流程
     */
    Object getApplyList(ProcessListReq queryParams);

    /**
     * 拒绝流程
     *
     * @param processRefuseReq 请求参数
     */
    void refuse(ProcessRefuseReq processRefuseReq);

    /**
     * 撤销流程
     *
     * @param processRevokeReq 请求参数
     */
    void revoke(ProcessRevokeReq processRevokeReq);

    /**
     * 抄送我的流程
     */
    Object getCcList(ProcessListReq queryParams);

    /**
     * 获取我已经审批的流程
     */
    Object getDoneList(ProcessListReq queryParams);

    /**
     * 查询实例动作记录
     */
    Object getInstanceAction(String instanceId);

    /**
     * 获取当前节点信息
     *
     * @param processId 流程定义id
     * @param nodeId    节点name
     */
    Object getNodeInfo(String processId, String nodeId);

    /**
     * 评论
     *
     * @param params 请求参数
     */
    void comment(ProcessCommentReq params);

    /**
     * 流程过程预览
     */
    Object processPreview(StartProcessReq params);

    /**
     * 查询实例表单数据
     */
    Object getInstanceForm(String instanceId);
}
