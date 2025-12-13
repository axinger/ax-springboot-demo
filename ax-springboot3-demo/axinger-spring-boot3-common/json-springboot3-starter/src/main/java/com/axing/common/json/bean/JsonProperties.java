package com.axing.common.json.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.jackson")
public class JsonProperties {

    /**
     * Date,LocalDateTime 格式
     */
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * LocalDate格式
     */
    private String localDateFormat = "yyyy-MM-dd";

    /**
     * LocalTime格式
     */
    private String localTimeFormat = "HH:mm:ss";
}
