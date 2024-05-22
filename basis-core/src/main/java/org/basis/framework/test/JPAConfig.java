//package org.basis.framework.test;
//
//import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
//import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
//import com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusProperties;
//import com.baomidou.mybatisplus.spring.boot.starter.SpringBootVFS;
//import org.apache.ibatis.datasource.pooled.PooledDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.mapper.MapperScannerConfigurer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//
//import javax.sql.DataSource;
//
//@Configuration
//@ComponentScan(basePackages = "com.xiaopeng.ins.erp.auth.service.mapper")
//public class JPAConfig{
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        return new PaginationInterceptor();
//    }
//
//
//    @Bean
//    public DataSource dataSource() {
//        PooledDataSource dataSource = new PooledDataSource();
//        dataSource.setUsername("xp_insurance_erp");
//        dataSource.setPassword("ZdWOHx1osAMpPuI_");
//        dataSource.setUrl("jdbc:mysql://rm-bp1x0ypop4kx4z2qbto.mysql.rds.aliyuncs.com:3306/xp_insurance_erp?" +
//                "useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2b8&autoReconnect=true&allowMultiQueries=true");
//        dataSource.setDriver("com.mysql.jdbc.Driver");
//        dataSource.setPoolMaximumIdleConnections(1);
//        dataSource.setPoolMaximumActiveConnections(1);
//        return dataSource;
//    }
//
//    @Bean("sqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//
//        MybatisPlusProperties properties = new MybatisPlusProperties();
//        properties.setConfigLocation("mybatis-config.xml");
//        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
//        factory.setDataSource(dataSource);
//        factory.setVfs(SpringBootVFS.class);
//        if (StringUtils.hasText(properties.getConfigLocation())) {
//            factory.setConfigLocation(new ClassPathResource(properties.getConfigLocation()));
//        }
//        properties.setTypeAliasesPackage("com.xiaopeng.ins.erp.auth.service.po");
//        if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
//            factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
//        }
//        properties.setTypeHandlersPackage("com.xiaopeng.ins.erp.auth.service.mapper");
//        if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
//            factory.setTypeHandlersPackage(properties.getTypeHandlersPackage());
//        }
//        properties.setMapperLocations(new String[]{"classpath:mybatis/mapper/*Mapper.xml"});
//        if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
//            factory.setMapperLocations(properties.resolveMapperLocations());
//        }
//        return factory.getObject();
//    }
//
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer(SqlSessionFactory sqlSessionFactory){
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setSqlSessionFactory(sqlSessionFactory);
//        mapperScannerConfigurer.setBasePackage("com.xiaopeng.ins.erp.auth.service.mapper");
//        return mapperScannerConfigurer;
//    }
//
//
//
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//}