package com.blog.api.service;

import com.blog.api.domain.Post;
import com.blog.api.exception.PostNotFound;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.request.PostSearch;
import com.blog.api.response.PostResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
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
        Post findPost = postRepository.findAll().get(0);

        //then
        assertEquals(writePost.getTitle() , findPost.getTitle());
        assertEquals(writePost.getContent() , findPost.getContent());

    }

    @Test
    @DisplayName("단건조회 시 글이없을 경우 Exception")
    void getPostException() {

        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        //expected
        PostNotFound ex = assertThrows(PostNotFound.class, () -> postService.getPost(post.getId() + 1L));
        assertEquals("존재하지 않는 글입니다.", ex.getMessage());

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

    @Test
    @DisplayName("게시글 조회[페이징 - 5개]")
    void getPosts() {

        //given
        List<Post> requestPosts = IntStream.rangeClosed(1, 30)
                .mapToObj(i -> Post.builder()
                            .title("생성제목 " + i)
                            .content("생성내용 " + i)
                            .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts); // 게시글 일괄저장

        Pageable pageable = PageRequest.of(0,5, Sort.Direction.DESC, "id");

        //when
        List<PostResponse> findPosts = postService.getPosts(pageable);

        //then
        assertEquals(5,findPosts.size());
        assertEquals("생성제목 30", findPosts.get(0).getTitle());
        assertEquals("생성제목 26", findPosts.get(4).getTitle());

    }

    @Test
    @DisplayName("게시글 조회[ Querydsl 페이징 - 10개]")
    void getList() {

        //given
        List<Post> requestPosts = IntStream.rangeClosed(1, 30)
                .mapToObj(i -> Post.builder()
                        .title("생성제목 " + i)
                        .content("생성내용 " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts); // 게시글 일괄저장

        PostSearch postSearch = new PostSearch(1,10);

        //when
        List<PostResponse> findPosts = postService.getList(postSearch);

        //then
        assertEquals(10,findPosts.size());
        assertEquals("생성제목 30", findPosts.get(0).getTitle());
        assertEquals("생성제목 21", findPosts.get(9).getTitle());

    }


    @Test
    @DisplayName("게시글 수정")
    void postEdit() {

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
        
        //when
        PostResponse resultPost = postService.edit(createPost.getId(), editPost);

        //then
        assertEquals(resultPost.getContent(), editPost.getContent());

    }

    @Test
    @DisplayName("게시글 삭제")
    void postDelete() {

        //given
        Post createPost = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(createPost);

        //when
        postService.delete(createPost.getId());

        //then
        assertEquals(0, postRepository.count());

    }

    
}