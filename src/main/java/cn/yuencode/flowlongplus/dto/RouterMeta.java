package cn.yuencode.flowlongplus.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 前端路由菜单meta
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
public class RouterMeta {
    private String title;
    private String i18nKey;
    private Boolean keepAlive;
    private Boolean constant;
    private String icon;
    private String localIcon;
    private Integer order;
    private String href;
    private String openType;
    private Boolean hideInMenu;
    private String activeMenu;
    private Boolean multiTab;
    private Integer fixedIndexInTab;
}
