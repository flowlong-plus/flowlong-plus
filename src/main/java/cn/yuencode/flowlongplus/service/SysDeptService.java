package cn.yuencode.flowlongplus.service;

import cn.yuencode.flowlongplus.controller.req.SysDeptPageParamReq;
import cn.yuencode.flowlongplus.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 部门 服务类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 查询部门分页列表
     *
     * @param params 查询参数
     * @return 列表数据
     */
    Object getList(SysDeptPageParamReq params);

    /**
     * 获取部门树
     *
     * @return 部门树
     */
    Object getDeptTree();

    /**
     * 新增部门
     *
     * @param sysDept 部门信息
     */
    void addDept(SysDept sysDept);

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     */
    void updateDept(SysDept sysDept);

    /**
     * 删除部门
     *
     * @param ids 部门id
     */
    void deleteDept(String[] ids);

    /**
     * 获取部门信息
     *
     * @param deptId 部门id
     * @return 部门信息
     */
    Object getDeptInfo(String deptId);
}
