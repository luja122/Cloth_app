package com.spring.CloathingStore.repo;

import com.spring.CloathingStore.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshtokenRepo extends JpaRepository<RefreshToken, UUID> {
}
