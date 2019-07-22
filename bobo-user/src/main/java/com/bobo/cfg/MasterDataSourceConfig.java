/*package com.bobo.cfg;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.bobo.cfg.db.ConsoleInterceptor;
import com.bobo.cfg.db.LikeInterceptor;

import lombok.Data;
import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@MapperScan(basePackages = MasterDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "masterSqlSessionFactory")
@ConfigurationProperties("datasource")
@Data
public class MasterDataSourceConfig {

    static final String PACKAGE = "com.bobo.user.mapper.master";
    static final String MAPPER_LOCATION = "classpath:mapper/master/*.xml";

    @Value("${master.datasource.url}")  
    private String url;  

    private String username;  

    private String password;  

    private String driverClassName;  

    private int initialSize;  

    private int minIdle;  

    private int maxActive;  

    private int maxWait;  

    private int timeBetweenEvictionRunsMillis;  

    private int minEvictableIdleTimeMillis;  

    private String validationQuery;  

    private boolean testWhileIdle;  

    private boolean testOnBorrow;  

    private boolean testOnReturn;  

    private boolean poolPreparedStatements;  

    private int maxPoolPreparedStatementPerConnectionSize;  

    private String filters;  

    private String connectionProperties;  


    @Bean(name = "masterDataSource")
    @Primary 
    public DataSource masterDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);  
        dataSource.setUsername(username);  
        dataSource.setPassword(password);  
        dataSource.setDriverClassName(driverClassName);  
        //具体配置 
        dataSource.setInitialSize(initialSize);  
        dataSource.setMinIdle(minIdle);  
        dataSource.setMaxActive(maxActive);  
        dataSource.setMaxWait(maxWait);  
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);  
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);  
        dataSource.setValidationQuery(validationQuery);  
        dataSource.setTestWhileIdle(testWhileIdle);  
        dataSource.setTestOnBorrow(testOnBorrow);  
        dataSource.setTestOnReturn(testOnReturn);  
        dataSource.setPoolPreparedStatements(poolPreparedStatements);  
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);  
        try {  
            dataSource.setFilters(filters);  
        } catch (SQLException e) { 
            e.printStackTrace();
        }  
        dataSource.setConnectionProperties(connectionProperties);  
        return dataSource;
    }

    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }

    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MasterDataSourceConfig.MAPPER_LOCATION));
        
        sessionFactory.setTypeAliasesPackage("com.jiaparts.*.model");
        sessionFactory.setPlugins(new Interceptor[] { new ConsoleInterceptor(), new LikeInterceptor() });
        
        return sessionFactory.getObject();
        
    }
}
*/