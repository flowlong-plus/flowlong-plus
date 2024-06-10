package cn.yuencode.flowlongplus.controller.req;

import lombok.Data;

/**
 * 用户登录请求参数
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Data
public class LoginReq {
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String verCode;
    /**
     * 租户id
     */
    private String tenantId;
}
