package cn.yuencode.flowlongplus.dto;

import cn.yuencode.flowlongplus.entity.SysPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * PermissionTreeItemDto
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Setter
@Getter
public class PermissionTreeItemDto extends SysPermission {
    List<PermissionTreeItemDto> children = new ArrayList<>();
}
