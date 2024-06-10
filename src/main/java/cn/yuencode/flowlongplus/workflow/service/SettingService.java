package cn.yuencode.flowlongplus.workflow.service;

import cn.yuencode.flowlongplus.workflow.controller.req.ProcessTemplateReq;
import cn.yuencode.flowlongplus.workflow.controller.res.TemplateGroupRes;

import java.util.List;

/**
 * 流程模版设置 服务类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface SettingService {

    /**
     * 查询模版分组
     */
    Object getTemplateGroups();

    /**
     * 模版分组排序
     *
     * @param groups 分组数据
     * @return 排序结果
     */
    Object templateGroupsSort(List<TemplateGroupRes> groups);

    /**
     * 修改分组
     *
     * @param id   分组ID
     * @param name 分组名
     * @return 修改结果
     */
    Object updateTemplateGroupName(String id, String name);

    /**
     * 新增表组
     *
     * @param name 分组名
     */
    Object createTemplateGroup(String name);

    /**
     * 删除分组
     *
     * @param id 分组ID
     * @return 删除结果
     */
    Object deleteTemplateGroup(Integer id);

    /**
     * 查询分组选项
     *
     * @return 数据
     */
    Object getGroupOptions();

    /**
     * 创建流程模板
     *
     * @param processTemplateReq 请求参数
     */
    void createTemplate(ProcessTemplateReq processTemplateReq);

    /**
     * 获取模板详情
     *
     * @param templateId 模板id
     * @return 详情数据
     */
    Object getTemplateDetail(String templateId);

    /**
     * 更新流程模板
     *
     * @param processTemplateReq 请求参数
     */
    void updateTemplate(ProcessTemplateReq processTemplateReq);
}
