package cn.yuencode.flowlongplus.service;

import cn.yuencode.flowlongplus.entity.SysStorageConfig;
import cn.yuencode.flowlongplus.util.page.PageParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 对象存储配置 服务类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface SysStorageConfigService extends IService<SysStorageConfig> {

    /**
     * 查询存储配置
     *
     * @param pageParam 分页参数
     */
    Object getList(PageParam pageParam);

    /**
     * 启用配置
     *
     * @param id 配置id
     */
    void switchEnable(String id, boolean enable);

    /**
     * 删除存储配置
     *
     * @param id id
     */
    void delete(String id);

    /**
     * 修改存储配置
     *
     * @param config 配置
     */
    void updateConfig(SysStorageConfig config);

    /**
     * 添加存储配置
     *
     * @param config 配置
     */
    void add(SysStorageConfig config);

    /**
     * 更新存储配置
     */
    void updateStorageConfigList();

    /**
     * 获取当前平台
     */
    String getActivePlatform();
}
