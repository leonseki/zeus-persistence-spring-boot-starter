package jp.tokyo.leon.zeus.persistence.spring.boot.starter.propertity;

import org.mybatis.spring.boot.autoconfigure.MybatisProperties;

/**
 * @author longtao.guan
 */
public class MybatisExtendProperties extends MybatisProperties {
    private String basePackage;

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
