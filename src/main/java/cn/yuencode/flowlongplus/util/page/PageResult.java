package cn.yuencode.flowlongplus.util.page;

import java.util.List;

/**
 * 分页响应
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public class PageResult<T> {
    /**
     * 当前页
     */
    private long pageNum;
    /**
     * 每页数量
     */
    private long pageSize;
    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
    private long totalPage;
    /**
     * 数据
     */
    private List<T> list;

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
