package cn.yuencode.flowlongplus.workflow.controller.req;

import lombok.Data;

import java.util.Map;

/**
 * 开启流程请求参数
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Data
public class StartProcessReq {
    /**
     * 模板id
     */
    private String templateId;
    /**
     * 实例变量
     */
    private Map<String, Object> variable;
}
