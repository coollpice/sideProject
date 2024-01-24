package com.blog.api.config.argumentresolver;

import com.blog.api.config.AppConfig;
import com.blog.api.config.data.UserSession;
import com.blog.api.exception.Unauthorized;
import com.blog.api.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(LoginUser.class); // 파라미터 어노테이션에 @LoginUser 인지 확인
        boolean hasMemberType = UserSession.class.isAssignableFrom(parameter.getParameterType()); // 파라미터 클래스가 UserSession 클래스인지 확인
        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String jws = request.getHeader("Authorization");
        if (jws == null || jws.isEmpty()) {
            throw new Unauthorized();
        }

        try {

            SecretKey secretKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());

            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jws);

            log.info(" >>>> {}", claimsJws);

            String userId = claimsJws.getPayload()
                    .getSubject();
            return new UserSession(Long.parseLong(userId));

        } catch (JwtException e) {
            throw new Unauthorized();
        }

        // Cookie 사용.
        /*
        Cookie[] cookies = request.getCookies();
        if (cookies.length == 0) {
            throw new Unauthorized();
        }


        String accessToken = "";

        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("SESSION")){
                accessToken = cookie.getValue();
            }
        }

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(Unauthorized::new);

        return UserSession.builder()
                .id(session.getUser().getId())
                .build();*/
    }
}
