package jp.tokyo.leon.zeus.persistence.spring.boot.starter.config;

import com.zaxxer.hikari.HikariDataSource;
import jp.tokyo.leon.zeus.persistence.spring.boot.starter.entity.ZeusPersistencePropertiesWrapper;
import jp.tokyo.leon.zeus.persistence.spring.boot.starter.propertity.MybatisExtendProperties;
import jp.tokyo.leon.zeus.persistence.spring.boot.starter.propertity.ZeusPersistenceProperties;
import jp.tokyo.leon.zeus.persistence.spring.boot.starter.utils.BeanNameUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static jp.tokyo.leon.zeus.persistence.spring.boot.starter.constant.CommonConstants.KEY_DATASOURCE;
import static jp.tokyo.leon.zeus.persistence.spring.boot.starter.constant.CommonConstants.PERSISTENCE_PREFIX;
import static jp.tokyo.leon.zeus.persistence.spring.boot.starter.constant.PropertyFieldConstants.*;


/**
 * @author longtao.guan
 */
@Configuration
public class ZeusDataSourceConfiguration implements EnvironmentAware, ImportBeanDefinitionRegistrar {

    private static final Class<HikariDataSource> DEFAULT_DATASOURCE_CLASS = HikariDataSource.class;
    private Map<String, Map<String, Map<String, String>>> persistencePropertiesCache;
    private Environment environment;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        ZeusPersistenceProperties zeusPersistenceProperties = parseZeusPersistenceProperties();
        List<String> persistenceNames = zeusPersistenceProperties.getPersistenceNames();
        for (String persistenceName : persistenceNames) {
            registerDatasource(registry, persistenceName, zeusPersistenceProperties.getDataSourceProperties(persistenceName));
            registerSqlSessionFactory(registry, persistenceName, zeusPersistenceProperties.getMybatisProperties(persistenceName));
            registerMapperScannerConfigurer(registry, persistenceName, zeusPersistenceProperties.getMybatisProperties(persistenceName));
        }
    }


    private ZeusPersistenceProperties parseZeusPersistenceProperties() {
        ZeusPersistenceProperties zeusPersistenceProperties = new ZeusPersistenceProperties();
        ZeusPersistencePropertiesWrapper zeusPersistencePropertiesWrapper = parseZeusPersistencePropertiesWrapper();
        List<String> persistenceNames = zeusPersistencePropertiesWrapper.getPersistenceNames();
        for (String persistenceName : persistenceNames) {
            DataSourceProperties dataSourceProperties = zeusPersistencePropertiesWrapper
                    .getPersistenceDataSourceProperties(persistenceName);
            MybatisExtendProperties mybatisProperties = zeusPersistencePropertiesWrapper
                    .getPersistenceMybatisProperties(persistenceName);
            zeusPersistenceProperties.addPersistenceProperties(
                    persistenceName, dataSourceProperties, mybatisProperties);
        }
        return zeusPersistenceProperties;
    }

    @SuppressWarnings("unchecked")
    private ZeusPersistencePropertiesWrapper parseZeusPersistencePropertiesWrapper() {
        Map<String, Map<String, Map<String, String>>> persistenceProperties;

        Binder binder = Binder.get(environment);
        persistenceProperties = binder.bind(PERSISTENCE_PREFIX, Bindable.of(Map.class)).get();
        persistencePropertiesCache = persistenceProperties;

        return new ZeusPersistencePropertiesWrapper(persistenceProperties);
    }

    private void registerDatasource(BeanDefinitionRegistry registry,
                                    String persistenceName,
                                    DataSourceProperties dataSourceProperties) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(
                Objects.nonNull(dataSourceProperties.getType()) ? dataSourceProperties.getType() : DEFAULT_DATASOURCE_CLASS);

        registry.registerBeanDefinition(persistenceName, beanDefinitionBuilder.getBeanDefinition());
    }

    private void registerSqlSessionFactory(BeanDefinitionRegistry registry,
                                           String persistenceName,
                                           MybatisExtendProperties mybatisProperties) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(SqlSessionFactoryBean.class);
        beanDefinitionBuilder.addPropertyReference(DATA_SOURCE, persistenceName);
        beanDefinitionBuilder.addPropertyValue(CONFIG_LOCATION, mybatisProperties.getConfigLocation());
        registry.registerBeanDefinition(BeanNameUtils.getSqlSessionFactoryName(persistenceName),
                beanDefinitionBuilder.getBeanDefinition());
    }

    private void registerMapperScannerConfigurer(BeanDefinitionRegistry registry,
                                                 String persistenceName,
                                                 MybatisExtendProperties mybatisProperties) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(MapperScannerConfigurer.class);
        beanDefinitionBuilder.addPropertyValue(SQL_SESSION_FACTORY_BEAN_NAME, BeanNameUtils.getSqlSessionFactoryName(persistenceName));
        beanDefinitionBuilder.addPropertyValue(BASE_PACKAGE, mybatisProperties.getBasePackage());
        registry.registerBeanDefinition(BeanNameUtils.getMapperScannerConfigurerName(persistenceName),
                beanDefinitionBuilder.getBeanDefinition());
    }

    public Map<String, String> getPersistenceDatasourceProperties(String persistenceName) {
        Map<String, Map<String, String>> persistenceProperties = persistencePropertiesCache.get(persistenceName);
        return persistenceProperties.get(KEY_DATASOURCE);
    }
}
