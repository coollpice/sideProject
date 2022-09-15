package com.blog.api.request;

import com.blog.api.exception.InvalidRequestException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (this.title.contains("공격")) {
            throw new InvalidRequestException("title", "제목에 공격이 포함될 수 없습니다.");
        }
    }

}
