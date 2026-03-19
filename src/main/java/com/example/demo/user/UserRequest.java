package com.example.demo.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class UserRequest {

    @Data
    public static class Join {
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Size(min = 4, max = 20, message = "아이디는 4~20자 사이여야 합니다.")
        private String username;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(min = 4, max = 20, message = "비밀번호는 4~20자 사이여야 합니다.")
        private String password;

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        private String postcode;
        private String address;
        private String detailAddress;
        private String extraAddress;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .postcode(postcode)
                    .address(address)
                    .detailAddress(detailAddress)
                    .extraAddress(extraAddress)
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
