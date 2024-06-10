package cn.yuencode.flowlongplus.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 前端路由菜单
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
public class RouterMenu {
    private String path;
    private String name;
    private String component;
    private String redirect;
    private RouterMeta meta;

    private List<RouterMenu> children;
}
