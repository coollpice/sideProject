package com.blog.api.controller;

import com.blog.api.domain.Session;
import com.blog.api.domain.User;
import com.blog.api.repository.SessionRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Login;
import com.blog.api.request.SignUp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccessTest() throws Exception {

        User loginUser = User.builder()
                .name("testName")
                .email("test@gmail.com")
                .password("1234")
                .build();

        userRepository.save(loginUser);

        Login login = Login.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

        String loginJson = objectMapper.writeValueAsString(login);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login") // 요청 방식 설정 [ GET, POST, PUT, DELETE 등 ]
                        .contentType(MediaType.APPLICATION_JSON) // CONTENT TYPE 설정 ( www.urlencoded , application/json 등 )
                        .content(loginJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.code").value(400))  // jsonpath 를 통해서 검증 가능하다.
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validateProp.title").value("제목을 입력하세요."))
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }

    @Transactional
    @Test
    @DisplayName("로그인 성공 후 세션 생성")
    void loginSuccessAfterCreateSessionTest() throws Exception {

        User loginUser = User.builder()
                .name("testName")
                .email("test@gmail.com")
                .password("1234")
                .build();

        userRepository.save(loginUser);

        Login login = Login.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

        String loginJson = objectMapper.writeValueAsString(login);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login") // 요청 방식 설정 [ GET, POST, PUT, DELETE 등 ]
                        .contentType(MediaType.APPLICATION_JSON) // CONTENT TYPE 설정 ( www.urlencoded , application/json 등 )
                        .content(loginJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.code").value(400))  // jsonpath 를 통해서 검증 가능하다.
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validateProp.title").value("제목을 입력하세요."))
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기

        Assertions.assertEquals(1L, loginUser.getSessions().size());
    }


    @Transactional
    @Test
    @DisplayName("로그인 성공 후 세션 객체 ")
    void loginSuccessAfterCreateSessionResponseTest() throws Exception {

        User loginUser = User.builder()
                .name("testName")
                .email("test@gmail.com")
                .password("1234")
                .build();

        userRepository.save(loginUser);

        Login login = Login.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

        String loginJson = objectMapper.writeValueAsString(login);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login") // 요청 방식 설정 [ GET, POST, PUT, DELETE 등 ]
                        .contentType(MediaType.APPLICATION_JSON) // CONTENT TYPE 설정 ( www.urlencoded , application/json 등 )
                        .content(loginJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))  // jsonpath 를 통해서 검증 가능하다.
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
//                .andExpect(jsonPath("$.validateProp.title").value("제목을 입력하세요."))
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기

        Assertions.assertEquals(1L, loginUser.getSessions().size());
    }


    @Test
    @DisplayName("로그인 후 권한 필요한 페이지 접속 /test")
    void loginAuthPageTest() throws Exception {

        User loginUser = User.builder()
                .name("testName")
                .email("test@gmail.com")
                .password("1234")
                .build();

        Session session = loginUser.addSession();
        userRepository.save(loginUser);


        mockMvc.perform(MockMvcRequestBuilders.get("/test") // 요청 방식 설정 [ GET, POST, PUT, DELETE 등 ]
                        .contentType(MediaType.APPLICATION_JSON) // CONTENT TYPE 설정 ( www.urlencoded , application/json 등 )
                        .header("Authorization", session.getAccessToken())
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }

    @Test
    @DisplayName("로그인 후 권한 필요한 페이지 접속2 /test")
    void loginAuthPageTest2() throws Exception {

        User loginUser = User.builder()
                .name("testName")
                .email("test@gmail.com")
                .password("1234")
                .build();

        Session session = loginUser.addSession();
        userRepository.save(loginUser);


        mockMvc.perform(MockMvcRequestBuilders.get("/test") // 요청 방식 설정 [ GET, POST, PUT, DELETE 등 ]
                        .contentType(MediaType.APPLICATION_JSON) // CONTENT TYPE 설정 ( www.urlencoded , application/json 등 )
                        .header("Authorization", session.getAccessToken().concat("-non"))
                )
                .andExpect(status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }

    @Test
    @DisplayName("회원가입")
    void signup() throws Exception {

        SignUp signUp = SignUp.builder()
                .email("test@gmail.com")
                .password("1234")
                .name("tester")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup") // 요청 방식 설정 [ GET, POST, PUT, DELETE 등 ]
                        .content(objectMapper.writeValueAsString(signUp))
                        .contentType(MediaType.APPLICATION_JSON) // CONTENT TYPE 설정 ( www.urlencoded , application/json 등 )
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }
}