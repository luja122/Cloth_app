package com.spring.CloathingStore.repo;

import com.spring.CloathingStore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {
 Optional<?> findByRole(String role) ;
}
