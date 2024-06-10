package cn.yuencode.flowlongplus.controller.req;

import lombok.Getter;
import lombok.Setter;

/**
 * 租户请求参数
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
public class SysTenantReq {
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 租户编码
     */
    private String tenantCode;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 开始时间
     */
    private String beginDate;
    /**
     * 结束时间
     */
    private String endDate;
    /**
     * 租户状态
     */
    private String status;
}