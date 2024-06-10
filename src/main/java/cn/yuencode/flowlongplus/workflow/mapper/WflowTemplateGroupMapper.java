package cn.yuencode.flowlongplus.workflow.mapper;

import cn.yuencode.flowlongplus.workflow.dto.TemplateGroupDto;
import cn.yuencode.flowlongplus.workflow.entity.WflowTemplateGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 流程模版分组关联 Mapper
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Mapper
public interface WflowTemplateGroupMapper extends BaseMapper<WflowTemplateGroup> {

    /**
     * 查询所有模版及分组
     */
    @Select("SELECT fg.group_id, tg.id, fg.group_name, pt.template_id, pt.template_key, pt.template_version, pt.remark, pt.status, pt.update_time, pt.template_name, " +
            "pt.logo FROM wflow_process_templates pt LEFT JOIN wflow_template_group tg ON pt.template_id = tg.template_id " +
            "RIGHT JOIN wflow_process_groups fg ON tg.group_id = fg.group_id " +
            "WHERE (pt.template_key, pt.template_version) in (select template_key, MAX(template_version) from wflow_process_templates group by template_key)" +
            "ORDER BY fg.sort_num ASC, tg.sort_num ASC")
    List<TemplateGroupDto> getAllFormAndGroups();
}
