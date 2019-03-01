package com.hailian.utils;

/**
 * 代码生成器
 * @author zuoqb
 * @since 2018-03-21
 */

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.hailian.entity.SysUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * @author zuoqb
 * @since 2018-03-21
 */
public class MpGenerator {
	public static void main(String[] args) {
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		gc.setOutputDir("D://");
		gc.setFileOverride(true);
		gc.setActiveRecord(true);// 不需要ActiveRecord特性的请改为false
		gc.setEnableCache(false);// XML 二级缓存
		gc.setBaseResultMap(true);// XML ResultMap
		gc.setBaseColumnList(true);// XML columList
		//gc.setKotlin(true) 是否生成 kotlin 代码
		gc.setAuthor("zuoqb");

		// 自定义文件命名，注意 %s 会自动填充表实体属性！
		// gc.setMapperName("%sDao");
		// gc.setXmlName("%sDao");
		// gc.setServiceName("MP%sService");
		// gc.setServiceImplName("%sServiceDiy");
		// gc.setControllerName("%sAction");
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(DbType.MYSQL);
		dsc.setTypeConvert(new MySqlTypeConvert() {
			// 自定义数据库表字段类型转换【可选】
			@Override
			public DbColumnType processTypeConvert(String fieldType) {
				System.out.println("转换类型：" + fieldType);
				// 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
				return super.processTypeConvert(fieldType);
			}
		});
		dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("hl123456");
		dsc.setUrl("jdbc:mysql://60.205.229.238:3306/springboot?characterEncoding=utf8");
		mpg.setDataSource(dsc);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		// strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
		strategy.setTablePrefix(new String[] { "" });// 此处可以修改为您的表前缀
		strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
		strategy.setInclude(new String[] {"etax_excel_file" }); // 需要生成的表
		// strategy.setExclude(new String[]{"test"}); // 排除生成的表

		// 自定义实体父类
		strategy.setSuperEntityClass("com.hailian.base.BaseModel");
		// 自定义实体，公共字段
		strategy.setSuperEntityColumns(new String[] { "create_by", "create_date", "update_by", "update_date",
				"remarks", "del_flag" });
		// 自定义 mapper 父类
		strategy.setSuperMapperClass("com.hailian.base.BaseMappers");
		// 自定义 service 父类
		strategy.setSuperServiceClass("com.hailian.base.BaseService");
		// 自定义 service 实现类父类
		strategy.setSuperServiceImplClass("com.hailian.base.BaseServiceImpl");
		// 自定义 controller 父类
		strategy.setSuperControllerClass("com.hailian.base.BaseController");
		strategy.setEntityLombokModel(false);//设置Lombok 设置了之后swagger接口不能传参数
		//strategy.setRestControllerStyle(true);//设置RestController
		// 【实体】是否生成字段常量（默认 false）
		// public static final String ID = "test_id";
		// strategy.setEntityColumnConstant(true);
		// 【实体】是否为构建者模型（默认 false）
		// public User setName(String name) {this.name = name; return this;}
		// strategy.setEntityBuilderModel(true);
		mpg.setStrategy(strategy);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent("com.hailian");
		pc.setController("controller"); 
		//        pc.setModuleName("entity");
		mpg.setPackageInfo(pc);

		// 自定义 xxList.jsp 生成
		List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
		// 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
				this.setMap(map);
			}
		};
		// 调整 xml 生成目录演示
		focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return "/develop/code/xml/" + tableInfo.getEntityName() + ".xml";
			}
		});

		// 自定义 xxListIndex.html 生成
		focList.add(new FileOutConfig("/templatesMybatis/list.html.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输入文件名称
				return "D://html//" + tableInfo.getEntityName() + "ListIndex.html";
			}
		});
		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);

		// 自定义  xxAdd.html 生成
		focList.add(new FileOutConfig("/templatesMybatis/add.html.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输入文件名称
				return "D://html//" + tableInfo.getEntityName() + "Add.html";
			}
		});
		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);

		//  自定义 xxUpdate.html生成
		focList.add(new FileOutConfig("/templatesMybatis/update.html.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输入文件名称
				return "D://html//" + tableInfo.getEntityName() + "Update.html";
			}
		});

		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);

		// 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
		// 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
		TemplateConfig tc = new TemplateConfig();
		// tc.setController("...");
		// tc.setEntity("...");
		// tc.setMapper("...");
		// tc.setXml("...");
		// tc.setService("...");
		// tc.setServiceImpl("...");
		// 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
		tc.setController("/templatesMybatis/controller.java.vm");
		tc.setService("/templatesMybatis/service.java.vm");
		tc.setServiceImpl("/templatesMybatis/serviceImpl.java.vm");
		tc.setEntity("/templatesMybatis/entity.java.vm");
		tc.setMapper("/templatesMybatis/mapper.java.vm");
		tc.setXml("/templatesMybatis/mapper.xml.vm");
		mpg.setTemplate(tc);

		// 执行生成
		mpg.execute();

		// 打印注入设置【可无】
		//        System.err.println(mpg.getCfg().getMap().get("abc"));
	}
}
