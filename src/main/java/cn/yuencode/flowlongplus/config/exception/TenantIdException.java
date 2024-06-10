package cn.yuencode.flowlongplus.config.exception;

/**
 * 租户Id异常
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public class TenantIdException extends RuntimeException {
    private String msg;

    public TenantIdException() {
        super();
    }

    public TenantIdException(String msg) {
        super(msg);
    }

    public TenantIdException(String msg, Throwable cause) {
        super(msg);
        super.initCause(cause);
    }

    public TenantIdException(Throwable cause) {
        super();
        super.initCause(cause);
    }
}
