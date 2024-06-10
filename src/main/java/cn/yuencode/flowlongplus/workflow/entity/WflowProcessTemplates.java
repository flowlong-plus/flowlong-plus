package cn.yuencode.flowlongplus.workflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 流程定义模版
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("wflow_process_templates")
@ApiModel(value = "WflowProcessTemplates对象", description = "wflow_process_templates")
public class WflowProcessTemplates implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("审批摸板ID")
    @TableId(value = "template_id", type = IdType.ASSIGN_ID)
    private String templateId;

    @ApiModelProperty("模版标识")
    private String templateKey;

    @ApiModelProperty("版本,默认1")
    private Integer templateVersion;

    @ApiModelProperty("模版名称")
    private String templateName;

    @ApiModelProperty("基础设置")
    private String settings;

    @ApiModelProperty("模版表单")
    private String formItems;

    @ApiModelProperty("流程定义")
    @TableField("`process`")
    private String process;

    @ApiModelProperty("图标url")
    private String logo;

    @ApiModelProperty("谁能提交")
    private String whoCommit;

    @ApiModelProperty("谁能编辑")
    private String whoEdit;

    @ApiModelProperty("谁能导出数据")
    private String whoExport;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("由wflow_template_group记录")
    private String groupId;

    @ApiModelProperty("0:停用;1:正常")
    @TableField("`status`")
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("workflow_id")
    private String workflowId;

    @ApiModelProperty("workflow流程版本")
    private Integer workflowVersion;

    @ApiModelProperty("租户id")
    private String tenantId;
}
