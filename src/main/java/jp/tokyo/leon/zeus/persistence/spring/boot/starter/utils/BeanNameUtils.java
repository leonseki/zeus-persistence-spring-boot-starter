package jp.tokyo.leon.zeus.persistence.spring.boot.starter.utils;

/**
 * @author longtao.guan
 */
public class BeanNameUtils {
    private static final String SQL_SESSION_FACTORY_SUFFIX = "SqlSessionFactory";
    private static final String MAPPER_SCANNER_CONFIGURER = "MapperScannerConfigurer";

    public static String getSqlSessionFactoryName(String originName) {
        return originName + SQL_SESSION_FACTORY_SUFFIX;
    }

    public static String getMapperScannerConfigurerName(String originName) {
        return originName + MAPPER_SCANNER_CONFIGURER;
    }
}
