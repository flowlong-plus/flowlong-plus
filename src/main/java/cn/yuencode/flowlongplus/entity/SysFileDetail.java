package cn.yuencode.flowlongplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件记录
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
@TableName("sys_file_detail")
@ApiModel(value = "FileDetail对象", description = "文件记录表")
public class SysFileDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("文件访问地址")
    private String url;

    @ApiModelProperty("文件大小，单位字节")
    private Long size;

    @ApiModelProperty("文件名称")
    private String filename;

    @ApiModelProperty("原始文件名")
    private String originalFilename;

    @ApiModelProperty("基础存储路径")
    private String basePath;

    @ApiModelProperty("存储路径")
    @TableField("`path`")
    private String path;

    @ApiModelProperty("文件扩展名")
    private String ext;

    @ApiModelProperty("MIME类型")
    private String contentType;

    @ApiModelProperty("存储平台")
    private String platform;

    @ApiModelProperty("缩略图访问路径")
    private String thUrl;

    @ApiModelProperty("缩略图名称")
    private String thFilename;

    @ApiModelProperty("缩略图大小，单位字节")
    private Long thSize;

    @ApiModelProperty("缩略图MIME类型")
    private String thContentType;

    @ApiModelProperty("文件所属对象id")
    private String objectId;

    @ApiModelProperty("文件所属对象类型，例如用户头像，评价图片")
    private String objectType;

    @ApiModelProperty("文件元数据")
    private String metadata;

    @ApiModelProperty("文件用户元数据")
    private String userMetadata;

    @ApiModelProperty("缩略图元数据")
    private String thMetadata;

    @ApiModelProperty("缩略图用户元数据")
    private String thUserMetadata;

    @ApiModelProperty("附加属性")
    private String attr;

    @ApiModelProperty("文件ACL")
    private String fileAcl;

    @ApiModelProperty("缩略图文件ACL")
    private String thFileAcl;

    @ApiModelProperty("哈希信息")
    private String hashInfo;

    @ApiModelProperty("上传ID，仅在手动分片上传时使用")
    private String uploadId;

    @ApiModelProperty("上传状态，仅在手动分片上传时使用，1：初始化完成，2：上传完成")
    private Integer uploadStatus;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("租户id")
    private String tenantId;
}
