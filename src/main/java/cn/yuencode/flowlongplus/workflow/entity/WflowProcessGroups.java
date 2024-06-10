package cn.yuencode.flowlongplus.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程分组
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
@TableName(value = "wflow_process_groups")
public class WflowProcessGroups implements Serializable {
    private static final long serialVersionUID = -50696449296875480L;

    @TableId(value = "group_id", type = IdType.ASSIGN_ID)
    private String groupId;
    /**
     * 组名
     */
    private String groupName;
    /**
     * 排序号
     */
    private Integer sortNum;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    private Date updated;

    /**
     * 租户id
     */
    private String tenantId;

}
