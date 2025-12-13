package com.github.axinger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ocr")
public class OcrProperties {

    private String pythonPath;
    private String scriptPath;
    private String uploadDir;

}
