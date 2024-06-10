package cn.yuencode.flowlongplus.enums;

/**
 * 开关状态枚举
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public enum EnableStatus {
    /**
     * 启用
     */
    enable(1),
    /**
     * 禁用
     */
    disable(2);

    private final int value;

    EnableStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
