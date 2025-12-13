package com.github.axinger.controller;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class LoginController {

    @GetMapping("/a")
    public Object index() {
        return List.of(1);
    }

    @GetMapping("/articles")
    public HashMap<Object, Object> search(Principal principal) {
        System.out.println("principal = " + principal);
        if (principal instanceof KeycloakPrincipal) {
            AccessToken accessToken = ((KeycloakPrincipal<?>) principal).getKeycloakSecurityContext().getToken();
            String preferredUsername = accessToken.getPreferredUsername();
            AccessToken.Access realmAccess = accessToken.getRealmAccess();
            Set<String> roles = realmAccess.getRoles();
            log.info("当前登录用户：{}, 角色：{}", preferredUsername, roles);
        }
        return null;
    }
}
