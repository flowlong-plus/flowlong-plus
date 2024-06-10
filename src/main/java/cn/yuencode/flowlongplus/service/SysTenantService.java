package cn.yuencode.flowlongplus.service;

import cn.yuencode.flowlongplus.controller.req.PageSysTenantReq;
import cn.yuencode.flowlongplus.controller.req.SysTenantReq;
import cn.yuencode.flowlongplus.entity.SysTenant;
import cn.yuencode.flowlongplus.util.page.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 租户信息 服务类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface SysTenantService extends IService<SysTenant> {

    /**
     * 获取租户选项列表
     *
     * @return 选项
     */
    Object options();

    /**
     * 更新租户信息
     *
     * @param sysTenantReq 请求参数
     */
    void updateTenant(SysTenantReq sysTenantReq);

    /**
     * 查询租户分页列表
     *
     * @param pageSysTenantReq 请求参数
     * @return 分页数据
     */
    PageResult<SysTenant> getList(PageSysTenantReq pageSysTenantReq);

    /**
     * 删除租户信息
     */
    void deleteIds(String[] ids);

    /**
     * 新增租户
     *
     * @param sysTenantReq 租户数据
     */
    void addTenant(SysTenantReq sysTenantReq);

    /**
     * 获取租户信息
     *
     * @param tenantId 租户id
     * @return 租户信息
     */
    Object getInfo(String tenantId);
}
