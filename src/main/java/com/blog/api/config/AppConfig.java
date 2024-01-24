package com.blog.api.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Base64;


@Getter
@ConfigurationProperties(prefix = "app-config")
@ConstructorBinding
public class AppConfig {

    private byte[] jwtKey;
    public AppConfig(String jwtKey) {
        this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }
}
