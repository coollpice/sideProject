package com.blog.api.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearch {

    private int page = 1;
    private int size = 10;

    private final int MAX_SIZE = 2000;

    public long getOffset() {
        return (long) (Math.max(1, this.page) -1 ) * Math.min(this.size , MAX_SIZE);
    }
}
