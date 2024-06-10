package cn.yuencode.flowlongplus.workflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程动作记录
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Getter
@Setter
@TableName(value = "wflow_instance_action_record", autoResultMap = true)
@ApiModel(value = "WflowInstanceActionRecord对象", description = "流程动作记录")
public class WflowInstanceActionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("流程实例id")
    private String instanceId;

    @ApiModelProperty("评论")
    @TableField("`comment`")
    private String comment;

    @ApiModelProperty("附件")
    @TableField(typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private List<String> attachments;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("抄送或者自动审批的话为true")
    private Boolean robot;

    @ApiModelProperty("审批办理人id")
    private String auditorId;

    @ApiModelProperty("动作类型")
    private Integer actionType;

    @ApiModelProperty("用户的ids,例如抄送的人可以放进来")
    @TableField(typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private List<String> userIds;

    @ApiModelProperty("分配人ID，例如加签给谁，减签给谁，转交给谁ID")
    private String assigneeId;

    @ApiModelProperty("flowlong中的nodeName,例如加签、减签、转交给谁，将当前节点记录一下")
    private String nodeId;

    @ApiModelProperty("租户id")
    private String tenantId;
}
