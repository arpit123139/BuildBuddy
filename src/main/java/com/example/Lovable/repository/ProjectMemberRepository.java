package com.example.Lovable.repository;

import com.example.Lovable.dto.member.MemberResponse;
import com.example.Lovable.entity.ProjectMember;
import com.example.Lovable.entity.ProjectMemberId;
import com.example.Lovable.enums.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findByIdProjectId(Long projectId);

    @Query("""
            select pm.projectRole from ProjectMember pm where pm.id=:projectMemberId
            """)
    Optional<ProjectRole> findRoleById(@Param("projectMemberId") ProjectMemberId projectMemberId);

    @Query("""
            select count(pm) from ProjectMember pm where 
            pm.id.userId = :userId and pm.projectRole = 'OWNER'
            """)
    int countProjectOwnedByUser(@Param("userId") Long userId);
}
