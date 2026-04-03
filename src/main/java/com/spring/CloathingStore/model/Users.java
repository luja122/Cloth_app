package com.spring.CloathingStore.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private UUID id;

    @NotBlank
    @Column(name="user_firstname",nullable = false)
    private String firstName;

    @Column(name="user_lastname",nullable = false)
    @NotBlank
    private String lastName;
    @NotBlank
    @Column(name="email",nullable = false, unique = true)
    private String email;
    @NotBlank
    @Column(name = "password",length = 8,nullable = false)
    private String password;
    @Lob //->Store large data in the database
    private byte[] imageData;

    private String imageType;

    public String imageName;

    private boolean isEnable=true;

    private Instant createdAt;

    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_table",
         joinColumns=@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> role = new HashSet<>();
    //just tell to store the data iside the enum in string
    @Enumerated(EnumType.STRING)
   private Provider provider=Provider.Local;

   @PrePersist
     void onCreate(){
       Instant now = Instant.now();
     if(createdAt == null){
         createdAt = now;
     }
     if(updatedAt==null){
         updatedAt = now;
     }

   }
}
