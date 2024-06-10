package cn.yuencode.flowlongplus.controller.res;

import cn.yuencode.flowlongplus.entity.SysDept;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 部门请求响应
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Setter
@Getter
@Builder
public class DeptRes extends SysDept {
    /**
     * 子部门
     */
    private List<DeptRes> children;
}
