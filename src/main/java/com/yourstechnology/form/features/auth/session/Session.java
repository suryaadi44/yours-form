package com.yourstechnology.form.features.auth.session;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.yourstechnology.form.features.auth.user.User;
import com.yourstechnology.form.utils.idGenerator.UUIDSequence;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "auth", name = "sessions", indexes = {
        @Index(columnList = "session_id", unique = true),
        @Index(columnList = "user_id")
})
public class Session {
    @Id
    @UUIDSequence
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private UUID sessionId;

    private Instant expiredAt;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}
