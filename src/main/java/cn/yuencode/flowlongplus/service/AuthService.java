package cn.yuencode.flowlongplus.service;

import cn.yuencode.flowlongplus.controller.req.LoginReq;

/**
 * AuthService
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface AuthService {
    /**
     * 登录
     *
     * @param params 请求参数
     * @return 授权信息
     */
    Object authLogin(LoginReq params);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取路由信息
     */
    Object getRouters();

    /**
     * 查询当前用户信息
     */
    Object getInfo();
}
