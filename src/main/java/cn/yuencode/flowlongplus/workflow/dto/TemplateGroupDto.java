package cn.yuencode.flowlongplus.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateGroupDto {

    private String id;

    private String groupId;

    private String groupName;

    private String templateId;

    private String templateName;

    private String logo;

    private Integer status;

    private String remark;

    private Date updateTime;
}
