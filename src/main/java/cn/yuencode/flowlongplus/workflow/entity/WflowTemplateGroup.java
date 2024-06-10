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
 * 流程与分组关联记录
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
@TableName(value = "wflow_template_group")
@ApiModel(value = "TemplateGroup对象", description = "流程与分组关联记录")
public class WflowTemplateGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("templateId")
    private String templateId;

    @ApiModelProperty("groupId")
    private String groupId;

    @ApiModelProperty("sortNum")
    private Integer sortNum;

    @ApiModelProperty("createTime")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("租户id")
    private String tenantId;
}
