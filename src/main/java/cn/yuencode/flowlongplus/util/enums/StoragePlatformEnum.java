package cn.yuencode.flowlongplus.util.enums;

/**
 * 对象存储配置枚举
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public enum StoragePlatformEnum {
    /**
     * 华为存储
     */
    HUAWEI_OBS("huawei-obs"),
    /**
     * 七牛云存储
     */
    QINIU_KODO("qiniu-kodo");

    private final String value;

    StoragePlatformEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
