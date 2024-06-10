package cn.yuencode.flowlongplus.workflow.mapper;

import cn.yuencode.flowlongplus.workflow.entity.WflowProcessTemplates;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 流程模版 Mapper
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Mapper
public interface WflowProcessTemplatesMapper extends BaseMapper<WflowProcessTemplates> {
    List<WflowProcessTemplates> getList(WflowProcessTemplates queryParams);
}
