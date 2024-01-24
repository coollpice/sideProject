package com.blog;

import com.blog.api.config.AppConfig;
import com.blog.api.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }


    @Bean
    @Profile("local")
    public TestDataInit testDataInit(UserRepository userRepository) {
        return new TestDataInit(userRepository);
    }

}
