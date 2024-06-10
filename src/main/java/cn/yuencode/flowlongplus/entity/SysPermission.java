package cn.yuencode.flowlongplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限菜单
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Getter
@Setter
@TableName("sys_permission")
@ApiModel(value = "SysPermission对象", description = "权限菜单")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("权限码")
    private String permissionCode;

    @ApiModelProperty("本权限的中文释义")
    private String permissionName;

    @ApiModelProperty("0=目录;1=菜单;2=按钮")
    private String menuType;

    @ApiModelProperty("self_=当前窗口;blank_=新窗口")
    private String openType;

    @ApiModelProperty("层级")
    @TableField("`level`")
    private Integer level;

    @ApiModelProperty("上级id")
    private String parentId;

    @ApiModelProperty("路由地址")
    private String routePath;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("菜单显示状态")
    private Boolean hideInMenu;

    @ApiModelProperty("菜单状态（1正常 2停用）")
    @TableField("`status`")
    private String status;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("是否缓存")
    private Boolean keepAlive;

    @ApiModelProperty("是否为外链")
    private Boolean isFrame;

    @ApiModelProperty("组件路径")
    @TableField("`component`")
    private String component;

    @ApiModelProperty("选中路由名称")
    private String activeMenu;

    @ApiModelProperty("是否固定多页签")
    private Integer fixedIndexInTab;

    @ApiModelProperty("路由名称")
    private String routeName;

    @ApiModelProperty("外部链接地址")
    private String href;

    private String title;

    private String i18nKey;

    private String localIcon;

    private Boolean multiTab;

    private Boolean constant;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    private String tenantId;


}
