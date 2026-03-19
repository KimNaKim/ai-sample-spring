package com.example.demo.user;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void save(UserRequest.Join requestDTO) {
        String hash = BCrypt.hashpw(requestDTO.getPassword(), BCrypt.gensalt());
        requestDTO.setPassword(hash);
        userRepository.save(requestDTO.toEntity());
    }

    @Transactional
    public UserResponse.Min login(UserRequest.Login requestDTO) {
        User user = userRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 틀렸습니다."));

        String dbPassword = user.getPassword();
        boolean isValid = false;

        // 1. BCrypt 형식($2a$...)인지 확인
        if (dbPassword.startsWith("$2a$")) {
            isValid = BCrypt.checkpw(requestDTO.getPassword(), dbPassword);
        } else {
            // 2. 평문일 경우 직접 비교 (마이그레이션 대상)
            isValid = dbPassword.equals(requestDTO.getPassword());
            
            // 3. 평문 로그인 성공 시 BCrypt로 암호화하여 마이그레이션 수행 (더티 체킹)
            if (isValid) {
                String hash = BCrypt.hashpw(requestDTO.getPassword(), BCrypt.gensalt());
                user.setPassword(hash);
            }
        }

        if (!isValid) {
            throw new RuntimeException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        return new UserResponse.Min(user);
    }

    @Transactional
    public UserResponse.Min update(Integer id, UserRequest.Update requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            String hash = BCrypt.hashpw(requestDTO.getPassword(), BCrypt.gensalt());
            user.setPassword(hash);
        }

        user.setEmail(requestDTO.getEmail());
        user.setPostcode(requestDTO.getPostcode());
        user.setAddress(requestDTO.getAddress());
        user.setDetailAddress(requestDTO.getDetailAddress());
        user.setExtraAddress(requestDTO.getExtraAddress());

        return new UserResponse.Min(user); // 더티 체킹으로 업데이트됨
    }

    public void sameCheck(String username) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new RuntimeException("이미 존재하는 아이디입니다.");
                });
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
    }
}
