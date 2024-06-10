/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package cn.yuencode.flowlongplus.assist;

import cn.hutool.core.util.ObjUtil;
import cn.yuencode.flowlongplus.config.exception.CommonException;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 断言辅助类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public abstract class Assert {
    /**
     * 断言表达式为true
     *
     * @param expression 判断条件
     * @param message    异常打印信息
     */
    public static void isTrue(boolean expression, String message) {
        illegal(expression, message);
    }

    public static void isFalse(boolean expression, Supplier<String> supplier) {
        illegal(!expression, supplier);
    }

    public static void isFalse(boolean expression, String message) {
        illegal(!expression, message);
    }

    public static void isZero(int result, String message) {
        illegal(Objects.equals(0, result), message);
    }

    /**
     * 断言表达式为true
     *
     * @param expression 判断条件
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be false");
    }


    /**
     * 断言给定的object对象为空
     *
     * @param object  待检测对象
     * @param message 异常打印信息
     */
    public static void isNull(Object object, String message) {
        illegal(null == object, message);
    }

    /**
     * 断言给定的object对象为空
     *
     * @param object 待检测对象
     */
    public static void isNull(Object object) {
        isNull(object, "[Assertion failed] - the object argument must not be null");
    }

    /**
     * 断言给定的object对象为非空
     *
     * @param object  待检测对象
     * @param message 异常打印信息
     */
    public static void notNull(Object object, String message) {
        illegal(null != object, message);
    }

    /**
     * 断言给定的object对象为非空
     *
     * @param object 待检测对象
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - the object argument must be null");
    }

    /**
     * 断言给定的object对象为空
     *
     * @param object 待检测对象
     */
    public static void isEmpty(Object object) {
        isEmpty(object, "[Assertion failed] - this argument must not be null or empty");
    }

    /**
     * 断言给定的object对象为空
     *
     * @param object  待检测对象
     * @param message 异常打印信息
     */
    public static void isEmpty(Object object, String message) {
        illegal(ObjUtil.isEmpty(object), message);
    }

    /**
     * 断言给定的object对象非空
     *
     * @param object  待检测对象
     * @param message 异常打印信息
     */
    public static void isNotEmpty(Object object, String message) {
        illegal(ObjUtil.isNotEmpty(object), message);
    }

    /**
     * 非法参数断言
     *
     * @param illegal 判断条件
     * @param message 提示内容
     */
    public static void illegal(boolean illegal, String message) {
        if (illegal) {
            throw throwable(message);
        }
    }

    /**
     * 非法参数断言
     *
     * @param illegal  判断条件
     * @param supplier 提示内容提供者
     */
    public static void illegal(boolean illegal, Supplier<String> supplier) {
        if (illegal) {
            throw throwable(supplier.get());
        }
    }

    /**
     * 非法参数断言
     *
     * @param message 提示内容
     */
    public static void illegal(String message) {
        throw throwable(message);
    }

    /**
     * 创建 CommonException 异常信息
     *
     * @param message 提示内容
     * @return {@link CommonException}
     */
    public static CommonException throwable(String message) {
        return new CommonException(message);
    }

    /**
     * 创建 CommonException 异常信息
     *
     * @param message 提示内容
     * @param cause   {@link Throwable}
     * @return {@link CommonException}
     */
    public static CommonException throwable(String message, Throwable cause) {
        return new CommonException(message, cause);
    }

    /**
     * 创建 CommonException 异常信息
     *
     * @param cause {@link Throwable}
     * @return {@link CommonException}
     */
    public static CommonException throwable(Throwable cause) {
        return new CommonException(cause);
    }
}
