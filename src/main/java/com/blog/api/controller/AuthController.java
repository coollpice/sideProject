package com.blog.api.controller;

import com.blog.api.config.AppConfig;
import com.blog.api.domain.User;
import com.blog.api.request.Login;
import com.blog.api.request.SignUp;
import com.blog.api.response.SessionResponse;
import com.blog.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/cookieLogin")
    public ResponseEntity cookieLogin(@RequestBody Login login) {
 /*       String accessToken = authService.login(login);
        log.info("accessToken => {}", accessToken);
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost") //TODO 설정파일에 지정
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();

        log.info("cookie >>> {}", cookie);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();*/
        return null;
    }

    @PostMapping("/auth/jwtLlogin")
    public SessionResponse login(@RequestBody Login login) {
        Long userId = authService.login(login);

        /*SecretKey key = Jwts.SIG.HS256.key().build();
        byte[] keyEncoded = key.getEncoded();
        String keyStr = Base64.getEncoder().encodeToString(keyEncoded);
*/

        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        log.info("keyStr {} ", key);

        String jws = Jwts.builder()
                .subject(String.valueOf(userId))
                .signWith(key)
                .issuedAt(new Date())
                .compact();
        return new SessionResponse(jws);
    }

    @PostMapping("/auth/signup")
    public SignUp signup(@RequestBody SignUp signUp) {
        authService.signup(signUp);
        return signUp;
    }

}
