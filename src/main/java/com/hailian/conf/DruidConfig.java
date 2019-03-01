package com.hailian.conf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

/**
 * Druid数据库连接池
 * 
 * @author : zuoqb
 * @date : 2018/05/08
 */
@Configuration
public class DruidConfig {
	@Value("${spring.datasource.url}")
	private String dbUrl;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;
	@Value("${spring.datasource.initialSize}")
	private int initialSize;
	@Value("${spring.datasource.minIdle}")
	private int minIdle;
	@Value("${spring.datasource.maxActive}")
	private int maxActive;
	@Value("${spring.datasource.maxWait}")
	private int maxWait;
	@Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;
	@Value("${spring.datasource.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;
	@Value("${spring.datasource.validationQuery}")
	private String validationQuery;
	@Value("${spring.datasource.testWhileIdle}")
	private boolean testWhileIdle;
	@Value("${spring.datasource.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${spring.datasource.testOnReturn}")
	private boolean testOnReturn;
	@Value("${spring.datasource.poolPreparedStatements}")
	private boolean poolPreparedStatements;
	@Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
	private int maxPoolPreparedStatementPerConnectionSize;
	@Value("${spring.datasource.filters}")
	private String filters;
	@Value("${spring.datasource.connectionProperties}")
	private String connectionProperties;
	@Value("${spring.datasource.useGlobalDataSourceStat}")
	private boolean useGlobalDataSourceStat;

	/**
	 * 注册一个StatViewServlet
	 *
	 * @return
	 */
	@Bean
	public ServletRegistrationBean druidStatViewServletBean() {
		// org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
				new StatViewServlet(), "/druid/*");

		// 添加初始化参数：initParams

		// 白名单：
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
		// IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not
		// permitted to view this page.
		// servletRegistrationBean.addInitParameter("deny", "192.168.0.151");
		// 登录查看信息的账号密码.
		// servletRegistrationBean.addInitParameter("loginUsername", "zuoqb");
		// servletRegistrationBean.addInitParameter("loginPassword", "zuoqb");
		// 是否能够重置数据.
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	/**
	 * druid监控过滤器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean druidStatFilterBean() {
		FilterRegistrationBean filter = new FilterRegistrationBean();
		filter.setFilter(new WebStatFilter());
		filter.setName("druidWebStatFilter");
		filter.addInitParameter("exclusions",
				"*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
		filter.addUrlPatterns("/*");
		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
		proxy.setTargetFilterLifecycle(true);
		proxy.setTargetBeanName("shiroFilter");
		return filter;
	}

	/**
	 * 如果不使用代码手动初始化DataSource的话，监控界面的SQL监控会没有数据("是spring boot的bug???")
	 * 
	 * @return 声明其为Bean实例,在同样的DataSource中，首先使用被标注的DataSource
	 */
	@Bean
	@Primary
	public DataSource dataSource() {
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(this.dbUrl);
		datasource.setUsername(username);
		datasource.setPassword(password);
		datasource.setDriverClassName(driverClassName);

		// configuration
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource
				.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		datasource
				.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		datasource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
		try {
			List filterList = new ArrayList<>();

			filterList.add(wallFilter());

			datasource.setProxyFilters(filterList);
			datasource.setFilters(filters);
		} catch (SQLException e) {
			System.err.println("druid configuration initialization filter: "
					+ e);
		}
		datasource.setConnectionProperties(connectionProperties);
		return datasource;
	}

	@Bean
	public WallFilter wallFilter() {

		WallFilter wallFilter = new WallFilter();

		wallFilter.setConfig(wallConfig());

		return wallFilter;

	}

	@Bean
	public WallConfig wallConfig() {

		WallConfig config = new WallConfig();

		config.setMultiStatementAllow(true);// 允许一次执行多条语句

		config.setNoneBaseStatementAllow(true);// 允许非基本语句的其他语句

		return config;

	}

}
