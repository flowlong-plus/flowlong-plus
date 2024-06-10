package cn.yuencode.flowlongplus.workflow.controller.req;

import lombok.Data;

/**
 * 流程模版
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Data
public class ProcessTemplateReq {
    /**
     * 流程模板id
     */
    private String templateId;
    /**
     * 流程模板名称
     */
    private String templateName;
    /**
     * 流程模板标识
     */
    private String templateKey;
    /**
     * 流程模板版本
     */
    private Integer templateVersion;
    /**
     * 流程分组id
     */
    private String groupId;
    /**
     * 流程表单定义
     */
    private String formItems;
    /**
     * 流程定义
     */
    private String process;
    /**
     * 流程设置
     */
    private String settings;
    /**
     * 备注
     */
    private String remark;
}
