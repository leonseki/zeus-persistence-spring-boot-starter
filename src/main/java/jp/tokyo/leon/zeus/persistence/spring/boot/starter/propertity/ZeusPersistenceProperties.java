package jp.tokyo.leon.zeus.persistence.spring.boot.starter.propertity;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.*;

/**
 * @author longtao.guan
 */
public class ZeusPersistenceProperties {

    private final Map<String, PersistenceProperties> persistencePropertiesMap = HashMap.newHashMap(16);

    public void addPersistenceProperties(String persistenceName,
                                         DataSourceProperties dataSourceProperties,
                                         MybatisExtendProperties mybatisProperties) {
        PersistenceProperties persistenceProperties = new PersistenceProperties(dataSourceProperties, mybatisProperties);
        persistencePropertiesMap.put(persistenceName, persistenceProperties);
    }

    public List<String> getPersistenceNames() {
        return new ArrayList<>(persistencePropertiesMap.keySet());
    }

    public PersistenceProperties getPersistenceProperties(String persistenceName) {
        return persistencePropertiesMap.get(persistenceName);
    }

    public DataSourceProperties getDataSourceProperties(String persistenceName) {
        PersistenceProperties persistenceProperties = persistencePropertiesMap.get(persistenceName);
        if (Objects.nonNull(persistenceProperties)) {
            return persistenceProperties.getDataSourceProperties();
        }
        throw new RuntimeException("persistence properties is null");
    }

    public MybatisExtendProperties getMybatisProperties(String persistenceName) {
        PersistenceProperties persistenceProperties = persistencePropertiesMap.get(persistenceName);
        if (Objects.nonNull(persistenceProperties)) {
            return persistenceProperties.getMybatisProperties();
        }
        throw new RuntimeException("mybatis properties is null");
    }

    public static class PersistenceProperties {
        private DataSourceProperties dataSourceProperties;
        private MybatisExtendProperties mybatisProperties;

        public PersistenceProperties(DataSourceProperties dataSourceProperties,
                                     MybatisExtendProperties mybatisProperties) {
            this.dataSourceProperties = dataSourceProperties;
            this.mybatisProperties = mybatisProperties;
        }

        public DataSourceProperties getDataSourceProperties() {
            return dataSourceProperties;
        }

        public void setDataSourceProperties(DataSourceProperties dataSourceProperties) {
            this.dataSourceProperties = dataSourceProperties;
        }

        public MybatisExtendProperties getMybatisProperties() {
            return mybatisProperties;
        }

        public void setMybatisProperties(MybatisExtendProperties mybatisProperties) {
            this.mybatisProperties = mybatisProperties;
        }
    }

}
