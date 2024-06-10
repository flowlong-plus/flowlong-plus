package cn.yuencode.flowlongplus.service;

import cn.yuencode.flowlongplus.controller.req.SysPostPageParmaReq;
import cn.yuencode.flowlongplus.entity.SysPost;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 岗位/职务 服务类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface SysPostService extends IService<SysPost> {

    /**
     * 查询岗位分页列表
     *
     * @param params 查询参数
     * @return 分页列表
     */
    Object getList(SysPostPageParmaReq params);

    /**
     * 新增岗位
     *
     * @param sysPost 岗位信息
     */
    void addPost(SysPost sysPost);

    /**
     * 更新岗位
     *
     * @param sysPost 岗位信息
     */
    void updatePost(SysPost sysPost);

    /**
     * 删除岗位
     *
     * @param ids 岗位id
     */
    void deletePost(String[] ids);

    /**
     * 获取岗位信息
     *
     * @param postId 岗位id
     * @return 岗位信息
     */
    Object getInfo(String postId);

    /**
     * 获取岗位下拉选项
     *
     * @return 岗位下拉选项
     */
    Object options();
}
