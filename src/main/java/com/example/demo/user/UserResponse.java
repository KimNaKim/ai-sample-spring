package com.example.demo.user;

import lombok.Data;

/**
 * RULE: Max, Min, Detail DTO이름을 최소, 최대, 상세를 기본으로 한다.
 */
public class UserResponse {

    @Data
    public static class Min {
        private Integer id;
        private String username;
        private String email;

        public Min(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }
}
