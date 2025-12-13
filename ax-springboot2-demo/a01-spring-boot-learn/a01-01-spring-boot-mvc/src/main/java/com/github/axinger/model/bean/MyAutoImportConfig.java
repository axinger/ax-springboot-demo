package com.github.axinger.model.bean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@AutoConfiguration
@Configuration
@EnableConfigurationProperties(value = {
        AxingerUserProperties.class,
//        AxingerPersonProperties.class,
        HumitureRuleProperties.class,
        MyYmlBean.class,
        ApplicationInfo.class,

        MyUserProperties.class
})
public class MyAutoImportConfig {

}
