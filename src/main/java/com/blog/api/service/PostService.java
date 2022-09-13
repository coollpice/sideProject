package com.blog.api.service;

import com.blog.api.domain.Post;
import com.blog.api.exception.PostNotFound;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.request.PostSearch;
import com.blog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.blog.api.domain.Post.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 글등록
     * @param postCreate
     * @return
     */
    public Post write(PostCreate postCreate) {
        Post post = builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        
        return postRepository.save(post);
    }

    /**
     * 글 단건조회
     * @param postId
     * @return
     */
    public PostResponse getPost(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        PostResponse response = PostResponse.builder()
                .id(findPost.getId())
                .title(findPost.getTitle())
                .content(findPost.getContent())
                .build();

        return response;
    }

    /**
     * 여러건 조회 - 페이징
     * @param pageable
     * @return
     */
    public List<PostResponse> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 여러건 조회 - 페이징 Querydsl
     * @param postSearch
     * @return
     */
    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long postId, PostEdit postEdit) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        /**
         * 1. 생성자를 이용한 수정 - 넘겨야할 파라메터가 많을경우, 순서에 신경써야한다...
         */
//        findPost.update(postEdit.getTitle(), postEdit.getContent());

        /**
         * 2. 빌더패턴을 이용하여 적용
         */
        PostEditor.PostEditorBuilder postEditorBuilder = findPost.toEditor();
        PostEditor editor = postEditorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        findPost.edit(editor);  // editor 객체를 넘겨서 수정

        return new PostResponse(findPost);
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                        .orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }

}
