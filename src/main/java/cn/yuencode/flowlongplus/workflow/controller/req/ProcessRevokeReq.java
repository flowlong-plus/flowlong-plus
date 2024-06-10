package cn.yuencode.flowlongplus.workflow.controller.req;

import lombok.Data;

import java.util.List;

/**
 * 流程审批请求参数 撤销流程
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Data
public class ProcessRevokeReq {
    /**
     * 流程实例id
     */
    private String instanceId;
    /**
     * comment
     */
    private String comment;
    /**
     * 附件 ids
     */
    private List<String> attachments;

}
