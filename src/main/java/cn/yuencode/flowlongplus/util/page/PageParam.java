package cn.yuencode.flowlongplus.util.page;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用分页参数
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
public class PageParam {
    /**
     * 当前页码
     */
    private Integer pageNum = 1;
    /**
     * 每页条数
     */
    private Integer pageSize = 10;
}
