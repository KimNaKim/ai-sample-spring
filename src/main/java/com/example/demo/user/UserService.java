package com.example.demo.user;

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
        userRepository.save(requestDTO.toEntity());
    }

    public UserResponse.Min login(UserRequest.Login requestDTO) {
        User user = userRepository.findByUsername(requestDTO.getUsername())
                .filter(u -> u.getPassword().equals(requestDTO.getPassword()))
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 틀렸습니다."));
        return new UserResponse.Min(user);
    }

    @Transactional
    public UserResponse.Min update(Integer id, UserRequest.Update requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        user.setPassword(requestDTO.getPassword());
        user.setEmail(requestDTO.getEmail());
        user.setPostcode(requestDTO.getPostcode());
        user.setAddress(requestDTO.getAddress());
        user.setDetailAddress(requestDTO.getDetailAddress());
        user.setExtraAddress(requestDTO.getExtraAddress());

        return new UserResponse.Min(user); // 더티 체킹으로 업데이트됨
    }
}
