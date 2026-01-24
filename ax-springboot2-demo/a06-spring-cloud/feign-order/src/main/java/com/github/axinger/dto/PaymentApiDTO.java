package com.github.axinger.dto;

import lombok.Data;

public class PaymentApiDTO {


    @Data
    public static class LoginDTO {
        private String userId;
        private String username;
        private String password;
    }

    @Data
    public static class LoginVO {
        private String token;
    }

    @Data
    public static class PaymentDTO {
        private String userId;
        private String orderId;
    }

    @Data
    public static class PaymentVO {
        private String orderId;
    }
}
