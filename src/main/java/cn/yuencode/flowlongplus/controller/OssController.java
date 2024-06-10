package cn.yuencode.flowlongplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.yuencode.flowlongplus.config.exception.CommonException;
import cn.yuencode.flowlongplus.entity.SysFileDetail;
import cn.yuencode.flowlongplus.service.SysFileDetailService;
import cn.yuencode.flowlongplus.service.SysStorageConfigService;
import cn.yuencode.flowlongplus.util.R;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 对象存储
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@Slf4j
@RequestMapping("/api/oss")
public class OssController {
    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private SysFileDetailService sysFileDetailService;
    @Resource
    private SysStorageConfigService sysStorageConfigService;

    @PostMapping("/upload")
    @SaCheckLogin
    public R<?> upload(@RequestPart MultipartFile file) {
        String platform = sysStorageConfigService.getActivePlatform();
        FileInfo upload = fileStorageService.of(file).setPath(DateUtil.format(new Date(), "yyyy/MM/dd") + "/").setPlatform(platform).upload();
        return R.success(BeanUtil.beanToMap(upload, "id", "size", "filename"));
    }

    /**
     * 文件预览/下载
     *
     * @param id 文件id
     */
    @GetMapping("/file/{id}")
    public void file(@PathVariable String id, @RequestParam(required = false, defaultValue = "false") Boolean download, HttpServletResponse response) {
        SysFileDetail sysFileDetail = sysFileDetailService.getById(id);
        if (ObjUtil.isNull(sysFileDetail)) {
            throw new CommonException("文件不存在");
        }
        FileInfo fileInfo = fileStorageService.getFileInfoByUrl(sysFileDetail.getUrl());
        //判断文件是否存在
        boolean exists = fileStorageService.exists(fileInfo);
        if (!exists) {
            throw new CommonException("文件不存在");
        }

        byte[] bytes = fileStorageService.download(fileInfo).bytes();
        try {
            ServletOutputStream os = response.getOutputStream();
            String filename = fileInfo.getFilename();
//            MediaType mediaType = MediaTypeFactory.getMediaType(filename).orElse(MediaType.APPLICATION_OCTET_STREAM);
            MediaType mediaType = MediaType.parseMediaType(fileInfo.getContentType());
            response.setContentType(mediaType.toString());
            response.setContentLength(fileInfo.getSize().intValue());

            // 如果不是图片类型，或download=true则设置Content-Disposition
            if ((!mediaType.isCompatibleWith(MediaType.IMAGE_JPEG) && !mediaType.isCompatibleWith(MediaType.IMAGE_PNG) && !mediaType.isCompatibleWith(MediaType.IMAGE_GIF)) || download) {
                if (download) {
                    response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                }
                response.setHeader("Content-Disposition", "inline;filename=\"" + filename + "\";filename*=utf8\"" + filename);
            }
            os.write(bytes, 0, bytes.length);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CommonException(e.getMessage());
        }
    }


    /**
     * 查询文件信息
     */
    @GetMapping("/fileInfo")
    @SaCheckLogin
    public R<?> fileInfo(@RequestParam String id) {
        SysFileDetail detail = sysFileDetailService.getById(id);
        if (ObjUtil.isNull(detail)) {
            R.fail("文件不存在");
        }
        return R.success(BeanUtil.beanToMap(detail, "id", "size", "filename", "originalFilename"));
    }
}
