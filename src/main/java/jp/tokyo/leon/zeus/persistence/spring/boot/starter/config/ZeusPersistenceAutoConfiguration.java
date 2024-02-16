package jp.tokyo.leon.zeus.persistence.spring.boot.starter.config;

import jp.tokyo.leon.zeus.persistence.spring.boot.starter.processor.DataSourceBeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author longtao.guan
 */
@AutoConfiguration
@Import({ZeusDataSourceConfiguration.class, DataSourceBeanPostProcessor.class})
public class ZeusPersistenceAutoConfiguration  {
}
