package cn.yuencode.flowlongplus.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 流程实例扩展数据
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
@TableName("wflow_ext_instance")
@ApiModel(value = "WflowExtInstance对象", description = "")
public class WflowExtInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("flowlong实例ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("租户ID")
    private String tenantId;

    @ApiModelProperty("流程模版id")
    private String templateId;

    @ApiModelProperty("flowlong流程id")
    private String processId;

    @ApiModelProperty("flowlong流程版本")
    private Integer processVersion;
}
