package com.blog.api.response;

import com.blog.api.domain.Post;
import lombok.Builder;
import lombok.Getter;

/**
 * 응답클래스.
 */
@Getter
public class PostResponse {

    private Long id;
    private String title;
    private String content;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0 , Math.min(10, title.length())); // 정책에 따른 Logic.
        this.content = content;
    }


}
