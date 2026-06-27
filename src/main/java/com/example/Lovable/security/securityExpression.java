package com.example.Lovable.security;

import com.example.Lovable.dto.auth.JwtUserPrincipal;
import com.example.Lovable.entity.ProjectMemberId;
import com.example.Lovable.enums.ProjectPermission;
import com.example.Lovable.enums.ProjectRole;
import com.example.Lovable.error.ResourceNotFoundException;
import com.example.Lovable.repository.ProjectMemberRepository;
import com.example.Lovable.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class securityExpression {

    private final AuthUtil authUtil;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    private boolean hasPermission(Long projectId,ProjectPermission projectPermission)
    {
        Long userId= authUtil.getCurrentUserId();
        ProjectMemberId projectMemberId=new ProjectMemberId(userId,projectId);

        return  projectMemberRepository.findRoleById(projectMemberId).map(role->role.getPermission().contains(projectPermission)).orElse(false);
    }

    public boolean canViewProject(Long projectId){
        return hasPermission(projectId,ProjectPermission.VIEW);
    }

    public boolean canEditProject(Long projectId){
        return hasPermission(projectId,ProjectPermission.EDIT);
    }

    public boolean canManageMember(Long projectId){
        return hasPermission(projectId,ProjectPermission.MANAGE_MEMBERS);
    }

    public boolean canViewMember(Long projectId){
        return hasPermission(projectId,ProjectPermission.VIEW_MEMBERS);
    }

    public boolean canDelete(Long projectId){
        return hasPermission(projectId,ProjectPermission.DELETE);
    }


}
