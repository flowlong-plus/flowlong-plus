package cn.yuencode.flowlongplus.workflow.controller.res;

import cn.yuencode.flowlongplus.workflow.dto.WFlowUserDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 我的申请
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
public class MyApplyRes implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 流程实例id
     */
    private String instanceId;
    /**
     * 流程模版id
     */
    private String processId;
    /**
     * 流程名称
     */
    private String processName;
    /**
     * 当前节点
     */
    private String currentNode;
    /**
     * 流程实例状态
     */
    private String instanceState;
    /**
     * 处理耗时
     */
    private Long duration;
    /**
     * 流程实例发起时间
     */
    private Date startTime;
    /**
     * 流程实例发起人
     */
    private WFlowUserDto startUser;
}
