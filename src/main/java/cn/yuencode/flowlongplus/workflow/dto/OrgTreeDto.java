package cn.yuencode.flowlongplus.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaoyu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgTreeDto {

    private String id;

    private String name;

    private String username;

    private String nickname;

    private String type;

    private String avatar;

    private String sex;

    private Boolean selected;

    private String tenantId;
}
