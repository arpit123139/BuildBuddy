 package com.example.Lovable.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

 @Data
 @NoArgsConstructor
 @AllArgsConstructor
 @Builder
 @Entity
 @Table(name = "users")
public class User implements UserDetails {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String  password;
    private String name;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
         return List.of();
     }

     @Override
     public String getPassword() {
         return this.password;
     }

     @Override
     public String getUsername() {
         return this.username;
     }
 }
