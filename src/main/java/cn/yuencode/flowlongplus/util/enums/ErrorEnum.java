package cn.yuencode.flowlongplus.util.enums;

/**
 * 错误码枚举
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public enum ErrorEnum {
    /**
     * 请求处理异常，请稍后再试
     */
    E_400(400, "请求处理异常，请稍后再试"),
    /**
     * 服务器错误,请联系管理员!
     */
    E_500(500, "服务器错误,请联系管理员!"),
    /**
     * 请求路径不存在
     */
    E_501(501, "请求路径不存在"),
    /**
     * 权限不足
     */
    E_502(502, "权限不足"),
    /**
     * 登录已过期,请重新登录
     */
    E_20001(20001, "登录已过期,请重新登录");


    private final Integer errorCode;

    private final String errorMsg;

    ErrorEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public Integer getErrorCode() {
        return errorCode;
    }


    public String getErrorMsg() {
        return errorMsg;
    }

}