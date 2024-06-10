package cn.yuencode.flowlongplus.controller.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * RoleInfoRes
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
@Builder
public class RoleInfoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 角色id
     */
    private String id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色描述
     */
    private String remark;
    /**
     * 状态
     */
    private String status;
    /**
     * 按钮权限
     */
    private List<String> permissionKeys;
    /**
     * 目录菜单权限
     */
    private List<String> permissions;
}
