package cn.yuencode.flowlongplus.workflow.dto;

import com.aizuda.bpm.engine.model.NodeModel;
import lombok.Data;

/**
 * 流程模版部署Dto
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Data
public class DeployDto {
    private String key;
    private String name;
    private String instanceUrl;
    /**
     * 流程定义
     */
    private NodeModel nodeConfig;
}
