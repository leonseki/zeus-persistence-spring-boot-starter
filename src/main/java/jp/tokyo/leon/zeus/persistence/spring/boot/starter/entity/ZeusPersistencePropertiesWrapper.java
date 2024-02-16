package jp.tokyo.leon.zeus.persistence.spring.boot.starter.entity;

import jp.tokyo.leon.zeus.persistence.spring.boot.starter.propertity.MybatisExtendProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static jp.tokyo.leon.zeus.persistence.spring.boot.starter.constant.CommonConstants.KEY_DATASOURCE;
import static jp.tokyo.leon.zeus.persistence.spring.boot.starter.constant.CommonConstants.KEY_MYBATIS;
/**
 * @author longtao.guan
 */
public class ZeusPersistencePropertiesWrapper {
    private Map<String, Map<String, Map<String, String>>> zeusPersistenceProperties;

    public ZeusPersistencePropertiesWrapper(Map<String, Map<String, Map<String, String>>> zeusPersistenceProperties) {
        this.zeusPersistenceProperties = zeusPersistenceProperties;
    }

    public Map<String, Map<String, Map<String, String>>> getZeusPersistenceProperties() {
        return zeusPersistenceProperties;
    }

    public void setZeusPersistenceProperties(Map<String, Map<String, Map<String, String>>> zeusPersistenceProperties) {
        this.zeusPersistenceProperties = zeusPersistenceProperties;
    }

    public List<String> getPersistenceNames() {
        return new ArrayList<>(zeusPersistenceProperties.keySet());
    }

    public DataSourceProperties getPersistenceDataSourceProperties(String persistenceName) {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        Map<String, Map<String, String>> persistenceProperties = zeusPersistenceProperties.get(persistenceName);
        Map<String, String> persistenceDatasourceProperties = persistenceProperties.get(KEY_DATASOURCE);
        if (Objects.nonNull(persistenceDatasourceProperties) && !persistenceDatasourceProperties.isEmpty()) {
            Binder binder = new Binder(new MapConfigurationPropertySource(persistenceDatasourceProperties));
            dataSourceProperties = binder.bind("", Bindable.of(DataSourceProperties.class)).get();
        }
        return dataSourceProperties;
    }

    public MybatisExtendProperties getPersistenceMybatisProperties(String persistenceName) {
        MybatisExtendProperties mybatisProperties = new MybatisExtendProperties();
        Map<String, Map<String, String>> persistenceProperties = zeusPersistenceProperties.get(persistenceName);
        Map<String, String> persistenceMybatisProperties = persistenceProperties.get(KEY_MYBATIS);
        if (Objects.nonNull(persistenceMybatisProperties) && !persistenceMybatisProperties.isEmpty()) {
            Binder binder = new Binder(new MapConfigurationPropertySource(persistenceMybatisProperties));
            mybatisProperties = binder.bind("", Bindable.of(MybatisExtendProperties.class)).get();
        }
        return mybatisProperties;
    }
}
