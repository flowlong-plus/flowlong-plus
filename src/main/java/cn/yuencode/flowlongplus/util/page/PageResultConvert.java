package cn.yuencode.flowlongplus.util.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页类型转换器
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public class PageResultConvert {

    /**
     * 将mybatis-plus类型转换为PageResult
     *
     * @param iPage IPage
     * @param <T>   数据类型
     * @return PageResult
     */
    public static <T> PageResult<T> convertMybatisPlus(IPage<T> iPage) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setPageNum(iPage.getCurrent());
        pageResult.setPageSize(iPage.getSize());
        pageResult.setTotal(iPage.getTotal());
        pageResult.setTotalPage(iPage.getTotal() / iPage.getSize() + 1);
        pageResult.setList(iPage.getRecords());
        return pageResult;
    }

    /**
     * 将PageHelper的PageInfo对象转换为PageResult
     *
     * @param pageInfo PageInfo
     * @param <T>      数据类型
     * @return PageResult
     */
    public static <T> PageResult<T> convertPageHepler(PageInfo<T> pageInfo) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setTotalPage(pageInfo.getTotal() / pageInfo.getPageSize() + 1);
        pageResult.setList(pageInfo.getList());
        return pageResult;
    }

    /**
     * 将PageHelper的PageInfo对象转换为PageResult
     *
     * @param list [Page extend List]
     * @param <T>  数据类型
     * @return PageResult
     */
    public static <T> PageResult<T> convertPageHelper(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return convertPageHepler(pageInfo);
    }

    /**
     * 将PageHelper的PageInfo对象转换为PageResult
     *
     * @param page Page
     * @param <T>  数据类型
     * @return PageResult
     */
    public static <T> PageResult<T> convertPageHelper(Page<T> page) {
        PageInfo<T> pageInfo = new PageInfo<>(page);
        return convertPageHepler(pageInfo);
    }
}
