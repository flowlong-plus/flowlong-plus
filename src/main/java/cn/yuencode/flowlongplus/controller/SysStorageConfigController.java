package cn.yuencode.flowlongplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.yuencode.flowlongplus.entity.SysStorageConfig;
import cn.yuencode.flowlongplus.service.SysStorageConfigService;
import cn.yuencode.flowlongplus.util.R;
import cn.yuencode.flowlongplus.util.enums.StoragePlatformEnum;
import cn.yuencode.flowlongplus.util.page.PageParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 对象存储配置
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@RequestMapping("/api/storageConfig")
public class SysStorageConfigController {
    @Resource
    private SysStorageConfigService sysStorageConfigService;

    /**
     * 查询存储配置
     */
    @GetMapping("/list")
    @SaCheckLogin
    public R<?> list(PageParam pageParam) {
        return R.success(sysStorageConfigService.getList(pageParam));
    }

    /**
     * 添加存储配置
     */
    @PostMapping("/add")
    @SaCheckLogin
    public R<?> add(@RequestBody SysStorageConfig config) {
        sysStorageConfigService.add(config);
        return R.success();
    }

    /**
     * 修改存储配置
     */
    @PutMapping("/update")
    @SaCheckLogin
    public R<?> update(@RequestBody SysStorageConfig config) {
        sysStorageConfigService.updateConfig(config);
        return R.success();
    }

    /**
     * 删除存储配置
     */
    @DeleteMapping("/delete")
    @SaCheckLogin
    public R<?> delete(@RequestBody SysStorageConfig config) {
        sysStorageConfigService.delete(config.getId());
        return R.success();
    }

    /**
     * 启用配置
     */
    @PutMapping("/switchEnable")
    @SaCheckLogin
    public R<?> enable(@RequestParam String id, @RequestParam boolean enable) {
        sysStorageConfigService.switchEnable(id, enable);
        return R.success();
    }

    /**
     * 平台选项
     */
    @GetMapping("/platform/options")
    @SaCheckLogin
    public R<?> options() {
        List<String> options = new ArrayList<>();
        for (StoragePlatformEnum platformEnum : StoragePlatformEnum.values()) {
            options.add(platformEnum.getValue());
        }
        return R.success(options);
    }

}
