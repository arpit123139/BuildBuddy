package com.example.Lovable.repository;

import com.example.Lovable.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

//    List<Project> findByUser(Long userId);

    @Query("""
            select p FROM Project p
            where p.deletedAt IS NULL
            AND (p.owner.id=:userId)
            ORDER BY p.updatedAt DESC
            """)
    List<Project> findALLAccessibleBYUser(@Param("userId") Long userId);

    @Query("""
            SELECT p from Project p
            where p.deletedAt IS NULL AND (p.owner.id=:userId) AND (p.id=:projectId)
            """)
    Optional<Project> getProjectById(@Param("userId")Long userId, @Param("projectId") Long projectId);
}
