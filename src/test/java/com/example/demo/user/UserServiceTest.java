package com.example.demo.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void save_test() {
        // given
        UserRequest.Join requestDTO = new UserRequest.Join();
        requestDTO.setUsername("ssar");
        requestDTO.setPassword("1234");
        requestDTO.setEmail("ssar@nate.com");

        // when
        userService.save(requestDTO);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        
        User savedUser = userCaptor.getValue();
        assertNotNull(savedUser.getPassword());
        assertTrue(BCrypt.checkpw("1234", savedUser.getPassword()));
    }

    @Test
    public void login_success_test() {
        // given
        UserRequest.Login requestDTO = new UserRequest.Login();
        requestDTO.setUsername("ssar");
        requestDTO.setPassword("1234");

        String hash = BCrypt.hashpw("1234", BCrypt.gensalt());
        User user = User.builder()
                .username("ssar")
                .password(hash)
                .email("ssar@nate.com")
                .build();

        when(userRepository.findByUsername("ssar")).thenReturn(Optional.of(user));

        // when
        UserResponse.Min responseDTO = userService.login(requestDTO);

        // then
        assertNotNull(responseDTO);
        assertTrue(responseDTO.getUsername().equals("ssar"));
    }

    @Test
    public void login_migration_test() {
        // given
        UserRequest.Login requestDTO = new UserRequest.Login();
        requestDTO.setUsername("ssar");
        requestDTO.setPassword("1234");

        // 평문으로 저장된 유저
        User user = User.builder()
                .username("ssar")
                .password("1234")
                .email("ssar@nate.com")
                .build();

        when(userRepository.findByUsername("ssar")).thenReturn(Optional.of(user));

        // when
        UserResponse.Min responseDTO = userService.login(requestDTO);

        // then
        assertNotNull(responseDTO);
        assertTrue(responseDTO.getUsername().equals("ssar"));
        
        // 마이그레이션 확인 (평문 "1234"가 BCrypt 해시로 바뀌었는지)
        assertTrue(user.getPassword().startsWith("$2a$"));
        assertTrue(BCrypt.checkpw("1234", user.getPassword()));
    }
}
