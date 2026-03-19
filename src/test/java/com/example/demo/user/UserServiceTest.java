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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void save_test() {
        // given
        UserRequest.Join requestDTO = new UserRequest.Join();
        requestDTO.setUsername("ssar");
        requestDTO.setPassword("1234");
        requestDTO.setEmail("ssar@nate.com");

        when(passwordEncoder.encode("1234")).thenReturn("hashed_password");

        // when
        userService.save(requestDTO);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        
        User savedUser = userCaptor.getValue();
        assertNotNull(savedUser.getPassword());
        assertTrue(savedUser.getPassword().equals("hashed_password"));
    }

    @Test
    public void login_success_test() {
        // given
        UserRequest.Login requestDTO = new UserRequest.Login();
        requestDTO.setUsername("ssar");
        requestDTO.setPassword("1234");

        User user = User.builder()
                .username("ssar")
                .password("hashed_password")
                .email("ssar@nate.com")
                .build();

        when(userRepository.findByUsername("ssar")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", "hashed_password")).thenReturn(true);

        // when
        UserResponse.Min responseDTO = userService.login(requestDTO);

        // then
        assertNotNull(responseDTO);
        assertTrue(responseDTO.getUsername().equals("ssar"));
    }
}
