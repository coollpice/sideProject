package com.blog.api.repository;

import com.blog.api.domain.Session;
import com.blog.api.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByAccessToken(String accessToken);
}
