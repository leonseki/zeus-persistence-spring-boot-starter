package jp.tokyo.leon.zeus.persistence.spring.boot.starter.processor;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import jp.tokyo.leon.zeus.persistence.spring.boot.starter.config.ZeusDataSourceConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

import static jp.tokyo.leon.zeus.persistence.spring.boot.starter.constant.CommonConstants.KEY_URL;

/**
 * @author longtao.guan
 */
public class DataSourceBeanPostProcessor implements BeanPostProcessor {

    private ZeusDataSourceConfiguration zeusDataSourceConfiguration;
    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        switch (bean) {
            case HikariDataSource hikariDataSource -> assembleHikariDataSource(hikariDataSource, beanName);
            case DruidDataSource druidDataSource -> assembleDruidDatasource(druidDataSource, beanName);
            case DataSource dataSource -> assembleTomcatJdbcDatasource(dataSource, beanName);
            default -> {
            }
        }
        return bean;
    }

    private void assembleHikariDataSource(HikariDataSource dataSource, String persistenceName) {
        Map<String, String> persistenceDatasourceProperties = zeusDataSourceConfiguration
                .getPersistenceDatasourceProperties(persistenceName);
        dataSource.setJdbcUrl(persistenceDatasourceProperties.get(KEY_URL));
        Binder binder = new Binder(new MapConfigurationPropertySource(persistenceDatasourceProperties));
        binder.bind("", Bindable.ofInstance(dataSource));
    }

    private void assembleDruidDatasource(DruidDataSource dataSource, String persistenceName) {
        Map<String, String> persistenceDatasourceProperties = zeusDataSourceConfiguration
                .getPersistenceDatasourceProperties(persistenceName);
        Binder binder = new Binder(new MapConfigurationPropertySource(persistenceDatasourceProperties));
        binder.bind("", Bindable.ofInstance(dataSource));
    }

    private void assembleTomcatJdbcDatasource(DataSource dataSource, String persistenceName) {
        Map<String, String> persistenceDatasourceProperties = zeusDataSourceConfiguration
                .getPersistenceDatasourceProperties(persistenceName);
        Binder binder = new Binder(new MapConfigurationPropertySource(persistenceDatasourceProperties));
        binder.bind("", Bindable.ofInstance(dataSource));
    }

}
