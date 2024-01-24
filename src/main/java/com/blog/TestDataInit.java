package com.blog;


import com.blog.api.domain.User;
import com.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final UserRepository userRepository;
// INSERT INTO `users` (name, email, password, creat_at) VALUES ('testName', 'test@gmail.com', 1234, '2023-12-01 13:00:00')
    /**
     * 확인용 초기 데이터 추가
     */
//    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        userRepository.save(
                User.builder()
                        .name("테스트")
                        .email("test@gamil.com")
                        .password("123")
                        .createAt(LocalDateTime.now())
                        .build()
        );
    }

}
