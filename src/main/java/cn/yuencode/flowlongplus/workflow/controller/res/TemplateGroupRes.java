package cn.yuencode.flowlongplus.workflow.controller.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分组模版列表信息
 *
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
public class TemplateGroupRes {

    private String id;

    private String name;

    private List<Template> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Template {

        private String tgId;

        private String templateId;

        private String templateName;

        private String logo;

        private Integer status;

        private String remark;

        private String updateTime;
    }
}
