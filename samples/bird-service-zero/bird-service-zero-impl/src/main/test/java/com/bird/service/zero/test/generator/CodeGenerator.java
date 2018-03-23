package com.bird.service.zero.test.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

/**
 * @author liuxx
 * @date 2018/3/23
 */
public class CodeGenerator {

    final String AUTHOR = "liuxx";
    final String PACKAGE_NAME = "com.bird.service.zero";
    final String MODULE_NAME = "user";
    final String OUTPUT_DIR = "D:\\Github\\JAVA\\bird-java\\samples\\bird-service-zero\\bird-service-zero-impl\\src\\main\\java";

    @Test
    public void generateCode(){
        generateCode("zero_user");
    }

    private void generateCode(String ...tableNames){
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(OUTPUT_DIR);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setAuthor(AUTHOR);

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("I%sService");
        gc.setServiceImplName("%sService");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("bird123456");
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/bird-zero?characterEncoding=utf8");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(new String[] { "zero_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
         strategy.setInclude(tableNames); // 需要生成的表
//         strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
         strategy.setSuperEntityClass("com.bird.service.common.model.AbstractModel");
        // 自定义实体，公共字段
         strategy.setSuperEntityColumns(new String[] { "id", "delFlag","createTime","modifiedTime" });
        // 自定义 mapper 父类
         strategy.setSuperMapperClass("com.bird.service.common.mapper.AbstractMapper");
        // 自定义 service 父类
         strategy.setSuperServiceClass("com.bird.service.common.service.IService");
        // 自定义 service 实现类父类
         strategy.setSuperServiceImplClass("com.bird.service.common.service.AbstractService");
        // 自定义 controller 父类
         strategy.setSuperControllerClass("com.bird.web.common.controller.AbstractController");
//         strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PACKAGE_NAME);
        pc.setModuleName("test");
        mpg.setPackageInfo(pc);

//        // 注入自定义配置，可以在 VM 中使用 cfg.abc
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
//                this.setMap(map);
//            }
//        };

//        // 自定义 xxList.jsp 生成
//        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
//        focList.add(new FileOutConfig("/template/list.jsp.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                return "D://my_" + tableInfo.getEntityName() + ".jsp";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//
//        // 调整 xml 生成目录演示
//        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return "/develop/code/xml/" + tableInfo.getEntityName() + ".xml";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);

        // 执行生成
        mpg.execute();
    }
}
