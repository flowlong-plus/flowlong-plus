package cn.yuencode.flowlongplus;

import cn.hutool.extra.spring.SpringUtil;
import cn.yuencode.flowlongplus.service.SysStorageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.StopWatch;

/**
 * App
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@SpringBootApplication
@EnableCaching
@EnableFileStorage
@Slf4j
public class App extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SpringApplication application = new SpringApplication(App.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
        stopWatch.stop();
        log.info("App启动成功 --> 启动时间: {}s", stopWatch.getTotalTimeSeconds());
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(App.class);
    }


    @Override
    public void run(String... args) {
        // 更新存储配置
        SysStorageConfigService sysStorageConfigService = SpringUtil.getBean(SysStorageConfigService.class);
        sysStorageConfigService.updateStorageConfigList();
    }
}
