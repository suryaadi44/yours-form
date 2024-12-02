package com.yourstechnology.form.features.auth.session;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    Optional<Session> findBySessionId(UUID sessionId);
}
