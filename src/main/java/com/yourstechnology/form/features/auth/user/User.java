package com.yourstechnology.form.features.auth.user;

import java.beans.Transient;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yourstechnology.form.utils.idGenerator.UUIDSequence;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(schema = "auth", name = "users", indexes = {
        @Index(columnList = "email", unique = true)
})
public class User implements UserDetails{
    @Id
    @UUIDSequence
    private UUID id;

    private String name;

    private String email;

    private Instant emailVerifiedAt;

    private String password;

    private String rememberToken;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

	@Transient
	public String getUsername() {
		return email;
	}
}
