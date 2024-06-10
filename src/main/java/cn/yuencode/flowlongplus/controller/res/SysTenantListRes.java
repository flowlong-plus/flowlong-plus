package cn.yuencode.flowlongplus.controller.res;

import lombok.Getter;
import lombok.Setter;

/**
 * SysTenantListRes
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
public class SysTenantListRes {
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
}
