package cn.yuencode.flowlongplus.service;

import cn.yuencode.flowlongplus.entity.SysFilePartDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.x.file.storage.core.upload.FilePartInfo;

/**
 * 文件分片信息表，仅在手动分片上传时使用 服务类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public interface SysFilePartDetailService extends IService<SysFilePartDetail> {
    void saveFilePart(FilePartInfo info);

    void deleteFilePartByUploadId(String uploadId);

    SysFilePartDetail toFilePartDetail(FilePartInfo info);
}
