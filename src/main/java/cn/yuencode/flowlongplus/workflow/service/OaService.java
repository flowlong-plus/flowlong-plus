package cn.yuencode.flowlongplus.workflow.service;

/**
 * 组织用户 服务类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface OaService {

    /**
     * 查询组织架构树
     *
     * @param deptId 部门id
     * @return 组织架构树数据
     */
    Object getOrgTreeData(Integer deptId);

    /**
     * 模糊搜索用户
     *
     * @param username 用户名
     * @return 匹配到的用户
     */
    Object getOrgTreeUser(String username);

    /**
     * 查询用户信息
     *
     * @param userId 用户id
     */
    Object getUserInfo(String userId);
}
