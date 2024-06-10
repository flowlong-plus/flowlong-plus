package cn.yuencode.flowlongplus.workflow.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 用户信息Dto
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
public class WFlowUserDto {
    /**
     * 用户id
     */
    private String id;
    /**
     * 用户名称
     */
    private String name;
}
