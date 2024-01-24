package com.blog.api.service;

import com.blog.api.domain.Session;
import com.blog.api.domain.User;
import com.blog.api.exception.AlreadyExistsUserException;
import com.blog.api.exception.InvalidSigninInformation;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Login;
import com.blog.api.request.SignUp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public Long login(Login login) {

        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        boolean matches = bCryptPasswordEncoder.matches(login.getPassword(), user.getPassword());

        if (!matches) {
            throw new InvalidSigninInformation();
        }

        return user.getId();
        /*Session session = user.addSession();
        return session.getAccessToken();*/
    }

    public void signup(SignUp signUp) {
        boolean exists = userRepository.existsByEmail(signUp.getEmail());
        if (exists) {
            throw new AlreadyExistsUserException();
        }

        String encodePassword = bCryptPasswordEncoder.encode(signUp.getPassword());
        var user = User.builder()
                .email(signUp.getEmail())
                .password(encodePassword)
                .name(signUp.getName())
                .build();
        userRepository.save(user);
    }
}
