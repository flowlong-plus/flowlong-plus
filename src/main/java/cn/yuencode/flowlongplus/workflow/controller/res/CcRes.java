package cn.yuencode.flowlongplus.workflow.controller.res;

import cn.yuencode.flowlongplus.workflow.dto.WFlowUserDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 抄送我的
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
public class CcRes implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
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
     * 流程实例id
     */
    private String instanceId;
    /**
     * 流程定义id
     */
    private String processId;
    /**
     * 流程任务状态
     */
    private String taskState;
    /**
     * 处理耗时
     */
    private Long duration;
    /**
     * 流程实例发起人
     */
    private WFlowUserDto startUser;
    /**
     * 流程实例发起时间
     */
    private Date startTime;
    /**
     * 任务开始时间
     */
    private Date taskStartTime;
}