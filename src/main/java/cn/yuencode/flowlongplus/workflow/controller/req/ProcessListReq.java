package cn.yuencode.flowlongplus.workflow.controller.req;

import cn.yuencode.flowlongplus.util.page.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程分页请求参数
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Setter
@Getter
public class ProcessListReq extends PageParam {
    /**
     * 流程名称
     */
    private String processName;
    /**
     * 流程实例状态
     */
    private Integer instanceState;
    /**
     * 流程分组
     */
    private String groupId;
}
