package com.blog.api.controller;

import com.blog.api.domain.Post;
import com.blog.api.request.PostCreate;
import com.blog.api.response.PostResponse;
import com.blog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }

    /**
     * 글등록
     * @param postCreate
     * @return
     */
    @PostMapping("/posts")
    public Long post(@RequestBody @Valid PostCreate postCreate) {
        Post savePost = postService.write(postCreate);
        return savePost.getId();
    }

    /**
     * 글 단건조회
     * @param postId
     * @return
     */
    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }
    
}
