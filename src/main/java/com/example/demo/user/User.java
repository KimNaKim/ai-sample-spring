package com.example.demo.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user_tb")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @Column(unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;
    private String email;

    // 주소 관련 필드 추가
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // RULE: 양방향 매핑 시 mappedBy를 사용하며, cascade를 통해 연관된 데이터도 함께 삭제한다.
    @jakarta.persistence.OneToMany(mappedBy = "user", cascade = jakarta.persistence.CascadeType.REMOVE, orphanRemoval = true)
    private java.util.List<com.example.demo.board.Board> boards;

    @jakarta.persistence.OneToMany(mappedBy = "user", cascade = jakarta.persistence.CascadeType.REMOVE, orphanRemoval = true)
    private java.util.List<com.example.demo.reply.Reply> replies;

    // RULE: 컬렉션은 생성자에 넣지 않는다.
    @Builder
    public User(Integer id, String username, String password, String email, String postcode, String address, String detailAddress, String extraAddress, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.postcode = postcode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.createdAt = createdAt;
    }

}
