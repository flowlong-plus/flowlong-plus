package cn.yuencode.flowlongplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件分片信息表，仅在手动分片上传时使用
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
@TableName("sys_file_part_detail")
@ApiModel(value = "FilePartDetail对象", description = "文件分片信息表，仅在手动分片上传时使用")
public class SysFilePartDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分片id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("存储平台")
    private String platform;

    @ApiModelProperty("上传ID，仅在手动分片上传时使用")
    private String uploadId;

    @ApiModelProperty("分片 ETag")
    private String eTag;

    @ApiModelProperty("分片号。每一个上传的分片都有一个分片号，一般情况下取值范围是1~10000")
    private Integer partNumber;

    @ApiModelProperty("文件大小，单位字节")
    private Long partSize;

    @ApiModelProperty("哈希信息")
    private String hashInfo;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("租户id")
    private String tenantId;
}
