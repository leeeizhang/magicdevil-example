package top.magicdevil.example.webapp.sample.bookstore.config;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String ENTITYS_PACKAGE = "top.magicdevil.example.webapp.sample.bookstore.**.entity.**";

    @Resource(name = "DBCPDataSource")
    private DataSource dataSource;

    @Bean("MyBatisSqlSessionFactoryBean")
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        {
            bean.setDataSource(dataSource);
            bean.setTypeAliasesPackage(ENTITYS_PACKAGE);
        }
        return bean;
    }

}
