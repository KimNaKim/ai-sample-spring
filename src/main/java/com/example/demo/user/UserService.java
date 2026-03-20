package com.example.demo.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(UserRequest.Join requestDTO) {
        String hash = passwordEncoder.encode(requestDTO.getPassword());
        requestDTO.setPassword(hash);
        userRepository.save(requestDTO.toEntity());
    }

    @Transactional
    public UserResponse.Min login(UserRequest.Login requestDTO) {
        User user = userRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 틀렸습니다."));

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        return new UserResponse.Min(user);
    }

    @Transactional
    public UserResponse.Min update(Integer id, UserRequest.Update requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            String hash = passwordEncoder.encode(requestDTO.getPassword());
            user.setPassword(hash);
        }

        user.setEmail(requestDTO.getEmail());
        user.setPostcode(requestDTO.getPostcode());
        user.setAddress(requestDTO.getAddress());
        user.setDetailAddress(requestDTO.getDetailAddress());
        user.setExtraAddress(requestDTO.getExtraAddress());

        return new UserResponse.Min(user); // 더티 체킹으로 업데이트됨
    }

    @Transactional
    public void withdraw(Integer id, UserRequest.Withdraw requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        userRepository.deleteById(id);
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
