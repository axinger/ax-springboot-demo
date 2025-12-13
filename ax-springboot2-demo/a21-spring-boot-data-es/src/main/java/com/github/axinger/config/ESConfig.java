package com.github.axinger.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName ESConfig.java
 * @Description TODO
 * @createTime 2022年02月24日 15:08:00
 */
@Configuration
@EnableConfigurationProperties(value = {EsProperties.class})
public class ESConfig {

    @Autowired
    private EsProperties esProperties;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
//
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esProperties.getUsername(), esProperties.getPassword()));
//
//
//        RestClient restClient = RestClient.builder(new HttpHost(esProperties.getAddress(), esProperties.getPort()))
//                .setHttpClientConfigCallback(builder -> {
//                    builder.setMaxConnPerRoute(esProperties.getMaxConnectPerRoute());
//                    builder.setMaxConnTotal(esProperties.getMaxConnectNum());
//                    return builder.setDefaultCredentialsProvider(credentialsProvider);
//                })
//
//                .build();
//
//
//        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//        ElasticsearchClient client = new ElasticsearchClient(transport);
//
//        return client;

        RestClient restClient = RestClient.builder(
                new HttpHost(esProperties.getAddress(), esProperties.getPort(), esProperties.getSchema())
        ).build();

        ElasticsearchClient client = new ElasticsearchClient(
                new RestClientTransport(
                        restClient,
                        new JacksonJsonpMapper()
                )
        );

        return client;
    }


}
