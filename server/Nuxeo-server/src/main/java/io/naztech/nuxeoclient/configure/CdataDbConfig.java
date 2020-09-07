package io.naztech.nuxeoclient.configure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CdataDbConfig {
	private static Logger log = LoggerFactory.getLogger(CdataDbConfig.class);
	private static CdataDbConfig cdataDbConfig = null;

	//@Value("${cdata.sage50uk.datasource.url}")
	private String url;

	//@Value("${cdata.sage50uk.datasource.username}")
	//private String userName;

	//@Value("${cdata.sage50uk.datasource.password}")
	//private String password;
	
	//@Value("${cdata.sage50uk.datasource.driver-class-name}")
	private String className;

	private Connection connection = null;
	
	private CdataDbConfig() {
		try {
			className = "cdata.jdbc.sage50uk.Sage50UKDriver";
			url = "jdbc:sage50uk:URL=http://naz-tech-pc-144:5495/sdata/accounts50/GCRM/{98F70C57-7AE2-4F48-BA17-F975DF42685D};User=manager;Password=n@ztech123;";
			Class.forName(className);
			connection = DriverManager.getConnection(url);
		}
		catch (SQLException | ClassNotFoundException e) {
			log.error("Cdata Connection is not established", e, e);
		}
	}
	
	public static CdataDbConfig getConfigInstance() {
		if(cdataDbConfig == null)
			cdataDbConfig = new CdataDbConfig();
		return cdataDbConfig;
	}
	
	public Connection getConnection() {
		return connection;
	}
}
