package com.github.axinger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * 资源服务器客户端
 * 使用RestTemplate调用资源服务器的受保护API
 */
@Service
@RequiredArgsConstructor
public class ResourceServerClient {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${resource.server.base-url:http://localhost:9001}")
    private String resourceServerBaseUrl;

    /**
     * 获取当前用户的访问令牌
     */
    private String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    oauthToken.getAuthorizedClientRegistrationId(),
                    oauthToken.getName()
            );
            if (client != null) {
                OAuth2AccessToken accessToken = client.getAccessToken();
                return accessToken.getTokenValue();
            }
        }
        return null;
    }

    /**
     * 构建带授权头的请求实体
     */
    private HttpEntity<Void> createAuthEntity() {
        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Object> createAuthEntity(Object body) {
        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    /**
     * 获取消息列表
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMessages() {
        HttpEntity<Void> entity = createAuthEntity();
        ResponseEntity<List> response = restTemplate.exchange(
                resourceServerBaseUrl + "/api/messages",
                HttpMethod.GET,
                entity,
                List.class
        );
        return (List<Map<String, Object>>) response.getBody();
    }

    /**
     * 创建消息
     */
    public Boolean createMessage(Map<String, Object> message) {
        HttpEntity<Object> entity = createAuthEntity(message);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                resourceServerBaseUrl + "/api/messages",
                HttpMethod.POST,
                entity,
                Boolean.class
        );
        return response.getBody();
    }

    /**
     * 获取当前用户信息
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserInfo() {
        HttpEntity<Void> entity = createAuthEntity();
        ResponseEntity<Map> response = restTemplate.exchange(
                resourceServerBaseUrl + "/api/user/info",
                HttpMethod.GET,
                entity,
                Map.class
        );
        return response.getBody();
    }
}
