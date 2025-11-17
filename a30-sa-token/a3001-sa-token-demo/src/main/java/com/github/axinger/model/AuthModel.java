package com.github.axinger.model;

import lombok.Data;

public class AuthModel {


    @Data
    public static class LoginDTO {
        private String username;
        private String password;
    }

    @Data
    public static class UserInfoDTO {
        private String userId;
        private String username;
        private String password;
        private OrganizationDTO org;
    }

    @Data
    public static class OrganizationDTO {

        /// 组织机构id
        private String id;

        /// 组织机构name
        private String name;

        /// 地区
        private String region;
    }
}
