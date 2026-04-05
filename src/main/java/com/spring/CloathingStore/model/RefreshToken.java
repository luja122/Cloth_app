package com.spring.CloathingStore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_tokens",indexes = {
        @Index(name = "refresh_token_jti_index",columnList = "jti",unique = true),
        @Index(name = "refresh_token_user_id_index",columnList = "user_id"),
        @Index(name ="refres_token_expires_At_index",columnList = "expires_At")
})
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name="jti",nullable = false,updatable = false,unique = true)
    private String jti;
    @ManyToOne(fetch = FetchType.LAZY,optional = false )
    @JoinColumn(name = "user_id",unique = true,nullable = false)
    private Users user;
    @Column(name = "created_At",nullable = false,updatable = false)
    private Instant created_At;
    @Column(name = "expires_At",nullable = false,updatable = false)
    private Instant  expires_At;

    private String replacedBy;
    private boolean revoked;



}
