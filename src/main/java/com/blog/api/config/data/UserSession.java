package com.blog.api.config.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSession {

    private Long id;

    @Builder
    public UserSession(Long id) {
        this.id = id;
    }
}
