package com.blog.api.service;

import com.blog.api.domain.Post;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.response.PostResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("게시글을 작성한다")
    void writeTest() {

        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        //when
        Post writePost = postService.write(postCreate);
        Post findPost = postRepository.findById(1L).get();

        //then
        assertEquals(writePost.getTitle() , findPost.getTitle());
        assertEquals(writePost.getContent() , findPost.getContent());

    }

    @Test
    @DisplayName("단건조회 시 글이없을 경우 Exception")
    void getPostException() {

        //given
        Long postId = 1L;

        //expected
        assertThrows(IllegalArgumentException.class, () -> postService.getPost(postId));

    }

    @Test
    @DisplayName("단건조회")
    void getPost() {

        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();
        Post writePost = postService.write(postCreate);
        Long postId = writePost.getId();

        //when
        PostResponse findPost = postService.getPost(postId);

        //then
        assertNotNull(findPost);
        assertEquals(writePost.getTitle() , findPost.getTitle());
        assertEquals(writePost.getContent() , findPost.getContent());

    }

    @Test
    @DisplayName("단건조회. 제목이 10글자가 넘을 시 10글자로")
    void getPostTitleLimit10() {

        //given
        PostCreate postCreate = PostCreate.builder()
                .title("123123123123123")
                .content("내용")
                .build();
        Post writePost = postService.write(postCreate);
        Long postId = writePost.getId();

        //when
        PostResponse findPost = postService.getPost(postId);

        //then
        assertNotNull(findPost);
        assertEquals(10, findPost.getTitle().length());
        assertEquals(writePost.getContent() , findPost.getContent());

    }
    
}