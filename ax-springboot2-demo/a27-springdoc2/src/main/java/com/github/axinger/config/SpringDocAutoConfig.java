package com.github.axinger.config;

import com.github.axinger.DocInfoProperties;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        DocInfoProperties.class
})
public class SpringDocAutoConfig {
    @Autowired
    private DocInfoProperties docInfo;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title(docInfo.getTitle()).description(docInfo.getDescription()).version(docInfo.getVersion())).externalDocs(new ExternalDocumentation().description(docInfo.getWebsiteName()).url(docInfo.getWebsiteUrl()));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder().group(docInfo.getTitle()).pathsToMatch("/**").build();
    }
}
