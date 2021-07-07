package top.magicdevil.example.webapp.sample.bookstore.config;

import java.io.Serializable;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBCPConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    @Value("${db.url}")
    private String url;
    
    @Value("${db.username}")
    private String username;
    
    @Value("${db.password}")
    private String password;

    @Bean("DBCPDataSource")
    public DataSource dataSource() throws Exception {
        BasicDataSource DBCPDataSource = new BasicDataSource();
        {
            DBCPDataSource.setDriverClassName(DRIVER);
            DBCPDataSource.setUrl(url);
            DBCPDataSource.setUsername(username);
            DBCPDataSource.setPassword(password);
        }
        return DBCPDataSource;
    }

}
