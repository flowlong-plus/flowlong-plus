package cn.yuencode.flowlongplus;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

/**
 * 代码生成器
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public class MybatisGenerator {
    public static void main(String[] args) {
        // 项目根路径
        final String projectPath = System.getProperty("user.dir");
        final String parentPackage = "cn.yuencode"; // 父包名
        final String currentModuleNamePackage = "flowlongplus";// 当前模块包名
        // 1、数据库配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/flowlong_engine?useSSL=false&useServerPrepStmts=true&characterEncoding=utf8", "root", "123456")
                .dbQuery(new MySqlQuery())
                .typeConvert(new MySqlTypeConvertCustom())
                .keyWordsHandler(new MySqlKeyWordsHandler())
                .build();

        // 2、全局配置
        String outputDir = projectPath + "/src/test/java";
        GlobalConfig globalConfig = new GlobalConfig.Builder()
                .fileOverride()
                .outputDir(outputDir)
                .author("jiaxiaoyu")
                // .enableKotlin() // 开启Kotlin模式
                .enableSwagger()
                .dateType(DateType.TIME_PACK)
                .commentDate("yyyy-MM-dd")
                .build();

        // 3、包配置
        PackageConfig packageConfig = new PackageConfig.Builder()
                .parent(parentPackage)
                .moduleName(currentModuleNamePackage)
                .entity("entity")
                .service("service")
                .serviceImpl("service.impl")
                .mapper("mapper")
                .xml("mapper.xml")
                .controller("controller")
//                .pathInfo(pathInfoMap)
                .build();

        // 4、策略配置
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
//                .addInclude("wflow_ext_instance")
                .addInclude("sys_dept", "sys_permission", "sys_post", "sys_role", "sys_role_permission",
                        "sys_tenant", "sys_user", "sys_user_role")
//                .addTablePrefix("wflow_")
                .entityBuilder()

                // 4.1、Entity 策略配置
                .enableLombok()
                .naming(NamingStrategy.underline_to_camel) // 下划线转驼峰
                .logicDeleteColumnName("delete_flag")
                // 用于自动填充为0
                .addTableFills(new Column("delete_flag", FieldFill.INSERT))
                .addTableFills(new Column("create_time", FieldFill.INSERT))
                .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                .addTableFills(new Column("create_by", FieldFill.INSERT))
                .addTableFills(new Column("update_by", FieldFill.INSERT_UPDATE))
                .idType(IdType.ASSIGN_ID)// 使用雪花算法
//                .idType(IdType.AUTO)// 使用自动递增

                // 4.1、Controller 策略配置
                .controllerBuilder()
                .enableRestStyle()// 开启生成@RestController 控制器

                // 4.2、Mapper 策略配置
                .mapperBuilder()
                .enableMapperAnnotation() // 开启@Mapper注解
                .enableBaseResultMap() // 启用 BaseResultMap 生成
                .enableBaseColumnList() // 启用 BaseColumnList

                .serviceBuilder()
                .formatServiceFileName("%sService")
                .build();
        // 5、创建代码生成器
        AutoGenerator mpg = new AutoGenerator(dataSourceConfig);
        mpg.global(globalConfig);
        mpg.packageInfo(packageConfig);
        mpg.strategy(strategyConfig);
        // 生成代码
        mpg.execute();
    }
}

class MySqlTypeConvertCustom extends MySqlTypeConvert implements ITypeConvert {
//    @Override
//    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
//        String t = fieldType.toLowerCase();
//        if (t.contains("tinyint")) {
//            return DbColumnType.INTEGER;
//        }
//        return super.processTypeConvert(globalConfig, fieldType);
//    }
}
