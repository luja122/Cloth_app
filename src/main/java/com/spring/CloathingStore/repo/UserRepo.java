package com.spring.CloathingStore.repo;

import com.spring.CloathingStore.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepo extends JpaRepository<Users, UUID> {
Optional<Users> findByEmail(String email);
}
