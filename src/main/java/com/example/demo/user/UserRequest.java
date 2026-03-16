package com.example.demo.user;

import lombok.Data;

public class UserRequest {

    @Data
    public static class Join {
        private String username;
        private String password;
        private String email;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();
        }
    }

    @Data
    public static class Login {
        private String username;
        private String password;
    }

    @Data
    public static class Update {
        private String password;
        private String email;
        private String postcode;
        private String address;
        private String detailAddress;
        private String extraAddress;
    }

}
