package com.blog.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class SignUp {

    private String email;
    private String password;
    private String name;

    @Builder
    public SignUp(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
