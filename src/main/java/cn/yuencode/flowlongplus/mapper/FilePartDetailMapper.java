package cn.yuencode.flowlongplus.mapper;

import cn.yuencode.flowlongplus.entity.SysFilePartDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文件分片信息表，仅在手动分片上传时使用 Mapper 接口
 * </p>
 *
 * @author jiaxiaoyu
 * @since 2024-05-16
 */
@Mapper
public interface FilePartDetailMapper extends BaseMapper<SysFilePartDetail> {

}
