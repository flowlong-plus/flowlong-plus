package cn.yuencode.flowlongplus.controller.req;

import cn.yuencode.flowlongplus.util.page.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 部门分页查询参数
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
public class SysDeptPageParamReq extends PageParam {
    /**
     * 部门名称
     */
    private String deptName;
}
