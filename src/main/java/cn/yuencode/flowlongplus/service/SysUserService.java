package cn.yuencode.flowlongplus.service;

import cn.yuencode.flowlongplus.controller.req.SysUserPageParamReq;
import cn.yuencode.flowlongplus.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户信息 服务类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 查询用户分页列表
     *
     * @param param 查询参数
     * @return 分页列表
     */
    Object getList(SysUserPageParamReq param);

    /**
     * 新增用户
     *
     * @param sysUser 用户信息
     */
    void addUser(SysUser sysUser);

    /**
     * 更新用户信息
     *
     * @param sysUser 用户信息
     */
    void updateUser(SysUser sysUser);

    /**
     * 删除用户
     *
     * @param ids 用户id
     */
    void deleteUser(String[] ids);

    /**
     * 查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    Object getInfo(String userId);
}
