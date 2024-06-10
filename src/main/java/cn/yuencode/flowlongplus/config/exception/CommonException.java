package cn.yuencode.flowlongplus.config.exception;

/**
 * 通用异常
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public class CommonException extends RuntimeException {
    private String msg;

    public CommonException() {
        super();
    }

    public CommonException(String msg) {
        super(msg);
    }

    public CommonException(String msg, Throwable cause) {
        super(msg);
        super.initCause(cause);
    }

    public CommonException(Throwable cause) {
        super();
        super.initCause(cause);
    }
}
