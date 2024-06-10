package cn.yuencode.flowlongplus.controller.req;

import cn.yuencode.flowlongplus.entity.SysRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 角色请求参数
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
public class SysRoleReq extends SysRole {
    /**
     * 权限列表ID集合(选中 ids)
     */
    List<String> permissions;
    /**
     * 权限列表ID集合(半选 ids)
     */
    List<String> permissionKeys;
}
