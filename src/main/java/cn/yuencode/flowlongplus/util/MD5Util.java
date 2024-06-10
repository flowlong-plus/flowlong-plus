package cn.yuencode.flowlongplus.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5加密工具类型
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public class MD5Util {
    public static String pwd_key = "flw_";

    /**
     * 密码加密
     */
    public static String getPassword(String pwd) {
        //此处加密后加盐再进行加密
        return DigestUtils.md5Hex(pwd_key + pwd);
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.getPassword("123456"));
    }

}