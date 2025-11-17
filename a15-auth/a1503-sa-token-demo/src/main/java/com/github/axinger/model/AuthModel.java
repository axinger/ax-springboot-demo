package com.github.axinger.model;

import lombok.Data;

public class AuthModel {

    @Data
    public static class LoginDTO {
        private String username;
        private String password;
    }

}
