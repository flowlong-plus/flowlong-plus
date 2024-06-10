package cn.yuencode.flowlongplus.service.impl;

import cn.yuencode.flowlongplus.entity.SysFilePartDetail;
import cn.yuencode.flowlongplus.mapper.FilePartDetailMapper;
import cn.yuencode.flowlongplus.service.SysFilePartDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.springframework.stereotype.Service;

/**
 * 文件分片信息表，仅在手动分片上传时使用 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Service
public class SysFilePartDetailServiceImpl extends ServiceImpl<FilePartDetailMapper, SysFilePartDetail> implements SysFilePartDetailService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 保存文件分片信息
     *
     * @param info 文件分片信息
     */
    @SneakyThrows
    public void saveFilePart(FilePartInfo info) {
        SysFilePartDetail detail = toFilePartDetail(info);
        if (save(detail)) {
            info.setId(detail.getId());
        }
    }

    /**
     * 删除文件分片信息
     */
    public void deleteFilePartByUploadId(String uploadId) {
        remove(new QueryWrapper<SysFilePartDetail>().eq("upload_id", uploadId));
    }

    /**
     * 将 FilePartInfo 转成 FilePartDetail
     *
     * @param info 文件分片信息
     */
    @SneakyThrows
    public SysFilePartDetail toFilePartDetail(FilePartInfo info) {
        SysFilePartDetail detail = new SysFilePartDetail();
        detail.setPlatform(info.getPlatform());
        detail.setUploadId(info.getUploadId());
        detail.setETag(info.getETag());
        detail.setPartNumber(info.getPartNumber());
        detail.setPartSize(info.getPartSize());
        detail.setHashInfo(valueToJson(info.getHashInfo()));
        detail.setCreateTime(info.getCreateTime());
        return detail;
    }

    /**
     * 将指定值转换成 json 字符串
     */
    public String valueToJson(Object value) throws JsonProcessingException {
        if (value == null) return null;
        return objectMapper.writeValueAsString(value);
    }
}
