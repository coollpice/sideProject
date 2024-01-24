package com.blog.api.controller;

import com.blog.api.domain.Post;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * 테스트 작성하기.
 * given - when - then 기법.
 *
 */

// @WebMvcTest  -- 웹영역만 테스트 시 사용
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;


    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts [POST] 요청 시 Title 값은 필수.")
    void postTest() throws Exception{

        PostCreate postCreate = PostCreate.builder()
                .content("내용입니다")
                .build();
        String postsCreateJson = objectMapper.writeValueAsString(postCreate);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts") // 요청 방식 설정 [ GET, POST, PUT, DELETE 등 ]
                        .contentType(MediaType.APPLICATION_JSON_VALUE) // CONTENT TYPE 설정 ( www.urlencoded , application/json 등 )
                        .content(postsCreateJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))  // jsonpath 를 통해서 검증 가능하다.
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validateProp.title").value("제목을 입력하세요."))
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }

    @Test
    @DisplayName("/posts [POST] 요청 시 저장.")
    void postsSaveTest() throws Exception {

        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();
        String postsCreateJson = objectMapper.writeValueAsString(postCreate);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .header("authorization", "test")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(postsCreateJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //then
        Post findPost = postRepository.findAll().get(0);

        assertEquals(findPost.getTitle(), postCreate.getTitle());
        assertEquals(findPost.getContent(), postCreate.getContent());

    }

    @Test
    @DisplayName("/posts/{postID} [GET] 글 단건조회")
    void getPost() throws Exception {

        //given
        Post createPost = Post.builder()
                .title("글 제목.")
                .content("글 내용.")
                .build();

        postRepository.save(createPost);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", createPost.getId())) // 요청 방식 설정 [ GET, POST, PUT, DELETE 등 ]
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(createPost.getId()))
                .andExpect(jsonPath("$.title").value(createPost.getTitle()))
                .andExpect(jsonPath("$.content").value(createPost.getContent()))
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }


    @Test
    @DisplayName("/posts [GET] 글 여러 건 조회 페이징")
    void getPosts() throws Exception {

        //given
        List<Post> requestPosts = IntStream.rangeClosed(1, 30)
                .mapToObj(i -> Post.builder()
                        .title("생성제목 " + i)
                        .content("생성내용 " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts); // 게시글 일괄저장

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&size=10") //appication.yml 에서 page보정. 원래 default 는 page인덱스는 0
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$.[0].title").value("생성제목 30"))
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }

    @Test
    @DisplayName("/posts [GET] 글 여러 건 조회 페이징 0 입력 시 1페이지 표출")
    void getPostsPage0() throws Exception {

        //given
        List<Post> requestPosts = IntStream.rangeClosed(1, 30)
                .mapToObj(i -> Post.builder()
                        .title("생성제목 " + i)
                        .content("생성내용 " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts); // 게시글 일괄저장

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=0") //appication.yml 에서 page보정. 원래 default 는 page인덱스는 0
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$.[0].title").value("생성제목 30"))
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }


    @Test
    @DisplayName("/posts [PATCH] 글 수정")
    void editPost() throws Exception {

        //given
        Post createPost = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(createPost); // 게시글 일괄저장

        PostEdit editPost = PostEdit.builder()
                .title("수정제목")
                .content("수정내용")
                .build();

        String editJson = objectMapper.writeValueAsString(editPost);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}",createPost.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(editJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title").value("수정제목"))
                .andExpect(jsonPath("$.content").value("수정내용"))
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }

    @Test
    @DisplayName("/posts [DELETE] 글 삭제")
    void deletePost() throws Exception {

        //given
        Post createPost = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(createPost);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}",createPost.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }

    @Test
    @DisplayName("/posts [DELETE] 글 삭제 - 없는 글 일 경우 예외")
    void deletePostException() throws Exception {

        //given
        Post createPost = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(createPost);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}",createPost.getId() + 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }


    @Test
    @DisplayName("/posts [POST] 글 등록 - 글 등록 시 제목에 '공격'은 포함될 수 없다.")
    void writePostValid() throws Exception {

        //given
        PostCreate postCreate = PostCreate.builder()
                .title("공격하라!")
                .content("내용입니다")
                .build();
        String postsCreateJson = objectMapper.writeValueAsString(postCreate);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(postsCreateJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print()); // HTTP 정보 로그 찍기
    }
}