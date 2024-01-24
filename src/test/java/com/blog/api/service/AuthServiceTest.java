package com.blog.api.service;

import com.blog.api.domain.User;
import com.blog.api.exception.AlreadyExistsUserException;
import com.blog.api.exception.InvalidSigninInformation;
import com.blog.api.repository.PostRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Login;
import com.blog.api.request.SignUp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.rmi.AlreadyBoundException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupTest() {
        SignUp signUp = SignUp.builder()
                .email("test@gmail.com")
                .password("1234")
                .name("tester")
                .build();

        authService.signup(signUp);

        Assertions.assertEquals(1, userRepository.count());

        User findUser = userRepository.findByEmail("test@gmail.com").get();

        Assertions.assertEquals(findUser.getEmail(), "test@gmail.com");
        Assertions.assertNotNull(findUser.getPassword());
        Assertions.assertEquals(findUser.getName(), "tester");
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 중복 이메일")
    void signupFailTestEmailAlreadyExists() {

        User user = User.builder()
                .email("test@gmail.com")
                .password("1234")
                .name("tester")
                .build();

        userRepository.save(user);

        SignUp signUp = SignUp.builder()
                .email("test@gmail.com")
                .password("1234")
                .name("tester")
                .build();

        Assertions.assertThrows(AlreadyExistsUserException.class,
                () -> authService.signup(signUp));
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {

        SignUp signUp = SignUp.builder()
                .email("test@gmail.com")
                .password("1234")
                .name("tester")
                .build();

        authService.signup(signUp);

        Login login = Login.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

        Long userId = authService.login(login);
        Assertions.assertNotNull(userId);
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void loginFailWrongPassword() {

        SignUp signUp = SignUp.builder()
                .email("test@gmail.com")
                .password("1234")
                .name("tester")
                .build();

        authService.signup(signUp);

        Login login = Login.builder()
                .email("test@gmail.com")
                .password("123")
                .build();

        Assertions.assertThrows(InvalidSigninInformation.class, () -> authService.login(login));
    }

}