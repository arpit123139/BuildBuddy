package com.example.Lovable.repository;

import com.example.Lovable.entity.Project;
import com.example.Lovable.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User,Long> {
}
