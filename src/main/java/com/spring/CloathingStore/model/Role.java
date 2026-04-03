package com.spring.CloathingStore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
@JoinColumn(name = "role_id",nullable = false,unique = true)
    //the join connect to the role and create a new table which have both user id and role id
    private UUID id;
    //role should be unique and cannot be null need to be user or admin or something
    @Column(unique = true,nullable = false)
    private String role;

}
