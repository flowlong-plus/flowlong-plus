package cn.yuencode.flowlongplus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.yuencode.flowlongplus.config.exception.CommonException;
import cn.yuencode.flowlongplus.entity.SysStorageConfig;
import cn.yuencode.flowlongplus.mapper.StorageConfigMapper;
import cn.yuencode.flowlongplus.service.SysStorageConfigService;
import cn.yuencode.flowlongplus.util.enums.StoragePlatformEnum;
import cn.yuencode.flowlongplus.util.page.PageParam;
import cn.yuencode.flowlongplus.util.page.PageResultConvert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 对象存储配置 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Slf4j
@Service
public class SysStorageConfigServiceImpl extends ServiceImpl<StorageConfigMapper, SysStorageConfig> implements SysStorageConfigService {
    @Resource
    private FileStorageService fileStorageService;

    @Override
    public Object getList(PageParam pageParam) {
        Page<SysStorageConfig> configPage = baseMapper.selectPage(new Page<>(pageParam.getPageNum(), pageParam.getPageSize()), null);
        return PageResultConvert.convertMybatisPlus(configPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void switchEnable(String id, boolean enable) {
        Long size = baseMapper.selectCount(null);
        if (size <= 1) {
            throw new CommonException("关闭失败，至少需要一个配置");
        }
        // 禁用原来配置
        List<SysStorageConfig> configs = baseMapper.selectList(Wrappers.<SysStorageConfig>lambdaQuery().eq(SysStorageConfig::getEnable, true));
        configs.forEach(c -> {
            c.setEnable(false);
            baseMapper.updateById(c);
        });
        // 启用配置
        SysStorageConfig config = baseMapper.selectById(id);
        config.setEnable(enable);
        baseMapper.updateById(config);
        updateStorageConfigList();
    }

    @Override
    public void delete(String id) {
        SysStorageConfig sysStorageConfig = baseMapper.selectById(id);
        if (ObjUtil.isNull(sysStorageConfig)) {
            throw new CommonException("未知的存储配置");
        }
        if (sysStorageConfig.getEnable()) {
            throw new CommonException("启用中的存储配置不能删除");
        }
        baseMapper.deleteById(id);
        updateStorageConfigList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(SysStorageConfig config) {
        if (config.getEnable()) {
            // 禁用原来配置
            List<SysStorageConfig> configs = baseMapper.selectList(Wrappers.<SysStorageConfig>lambdaQuery().eq(SysStorageConfig::getEnable, true));
            configs.forEach(c -> {
                c.setEnable(false);
                baseMapper.updateById(c);
            });
        }
        baseMapper.updateById(config);
        updateStorageConfigList();
    }

    @Override
    public void add(SysStorageConfig config) {
        baseMapper.insert(config);
        updateStorageConfigList();
    }

    @Override
    public void updateStorageConfigList() {
        List<SysStorageConfig> configs = baseMapper.selectList(Wrappers.<SysStorageConfig>lambdaQuery().eq(SysStorageConfig::getEnable, true));
        if (configs.size() != 1) {
            throw new CommonException("存储配置错误");
        }
        SysStorageConfig sysStorageConfig = configs.get(0);
        CopyOnWriteArrayList<FileStorage> list = fileStorageService.getFileStorageList();
        // 清空所有配置
        list.clear();
        // 七牛云存储
        if (sysStorageConfig.getPlatform().equals(StoragePlatformEnum.QINIU_KODO.getValue())) {
            FileStorageProperties.QiniuKodoConfig qiniuKodoConfig = BeanUtil.toBean(sysStorageConfig.getConfig(), FileStorageProperties.QiniuKodoConfig.class);
            qiniuKodoConfig.setPlatform(StoragePlatformEnum.QINIU_KODO.getValue());
            list.addAll(FileStorageServiceBuilder.buildQiniuKodoFileStorage(Collections.singletonList(qiniuKodoConfig), null));
        }
        log.info("更新存储配置成功");
    }

    @Override
    public String getActivePlatform() {
        List<SysStorageConfig> configs = baseMapper.selectList(Wrappers.<SysStorageConfig>lambdaQuery().eq(SysStorageConfig::getEnable, true));
        if (configs.size() != 1) {
            throw new CommonException("存储配置错误");
        }
        SysStorageConfig sysStorageConfig = configs.get(0);
        return sysStorageConfig.getPlatform();
    }
}
