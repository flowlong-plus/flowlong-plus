package cn.yuencode.flowlongplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 部门
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
@TableName("sys_dept")
@ApiModel(value = "SysDept对象", description = "部门")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "dept_id", type = IdType.ASSIGN_ID)
    private String deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门层级数")
    @TableField("`level`")
    private Integer level;

    @ApiModelProperty("上级部门id")
    private String parent;

    @ApiModelProperty("部门子管理员")
    private String deputyLeaders;

    @ApiModelProperty("部门主管理员")
    private String mainLeader;

    @ApiModelProperty("排序")
    private Integer orderNum;

    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("租户id")
    private String tenantId;


}
