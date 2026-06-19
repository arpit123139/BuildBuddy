package com.example.Lovable.repository;

import com.example.Lovable.dto.member.MemberResponse;
import com.example.Lovable.entity.ProjectMember;
import com.example.Lovable.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findByIdProjectId(Long projectId);
}
