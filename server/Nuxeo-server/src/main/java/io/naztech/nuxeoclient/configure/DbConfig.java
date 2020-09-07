package io.naztech.nuxeoclient.configure;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.nazdaqTechnologies.jdbc.JdbcService;
import com.nazdaqTechnologies.jdbc.JdbcStatementFactory;

/**
 * Databse Configuartion according to Spring Java based DB Config with JDBC.
 * 
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-09-11
 */
@Configuration
@ComponentScan("io.naztech.nuxeoclient")
public class DbConfig {

	@Value("${jdbc.driver}")
	private String dbDriver;

	@Value("${jdbc.url}")
	private String dbUrl;

	@Value("${jdbc.username}")
	private String dbUser;

	@Value("${jdbc.password}")
	private String dbPass;

	@Bean
	JdbcService jdbcService() {
		JdbcService jService = new JdbcService();
		jService.setDataSource(dataSource());
		jService.setTransactionManager(dataSourceTransactionManager());
		jService.setJdbcStatementFactory(new JdbcStatementFactory());
		return jService;
	}

	@Bean("dataSource")
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(dbDriver);
		ds.setUrl(dbUrl);
		ds.setUsername(dbUser);
		ds.setPassword(dbPass);
		ds.setInitialSize(5);
		ds.setMaxActive(-1);
		ds.setMaxIdle(10);
		ds.setDefaultAutoCommit(true);
		return ds;
	}

	@Bean
	DataSourceTransactionManager dataSourceTransactionManager() {
		DataSourceTransactionManager obj = new DataSourceTransactionManager();
		obj.setDataSource(dataSource());
		return obj;
	}

	@Bean
	public static EnvironmentStringPBEConfig environmentVariablesConfiguration() {
		EnvironmentStringPBEConfig environmentVariablesConfiguration = new EnvironmentStringPBEConfig();
		environmentVariablesConfiguration.setAlgorithm("PBEWithMD5AndDES");
		environmentVariablesConfiguration.setPasswordEnvName("APP_ENCRYPTION_PASSWORD");
		environmentVariablesConfiguration.setPassword("pSqi7E1HywDZGlv6qJz67rPMDORKL8M3NGEnfQ==");
		return environmentVariablesConfiguration;
	}

	@Bean
	public static StringEncryptor configurationEncryptor() {
		StandardPBEStringEncryptor configurationEncryptor = new StandardPBEStringEncryptor();
		configurationEncryptor.setConfig(environmentVariablesConfiguration());
		return configurationEncryptor;
	}

	@Bean
	public static PropertyPlaceholderConfigurer propertyConfigurer() {
		EncryptablePropertyPlaceholderConfigurer propertyConfigurer = new EncryptablePropertyPlaceholderConfigurer(
				configurationEncryptor());
		propertyConfigurer.setLocations(new ClassPathResource("application.properties"),
				new ClassPathResource("invoice.properties"));
		return propertyConfigurer;
	}

}
