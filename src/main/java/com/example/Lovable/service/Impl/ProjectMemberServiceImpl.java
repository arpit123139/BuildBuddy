package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.member.InviteMemberRequest;
import com.example.Lovable.dto.member.MemberResponse;
import com.example.Lovable.dto.member.UpdateRoleRequest;
import com.example.Lovable.entity.Project;
import com.example.Lovable.entity.ProjectMember;
import com.example.Lovable.entity.ProjectMemberId;
import com.example.Lovable.entity.User;
import com.example.Lovable.error.ResourceNotFoundException;
import com.example.Lovable.mapper.ProjectMemberMapper;
import com.example.Lovable.repository.ProjectMemberRepository;
import com.example.Lovable.repository.ProjectRepository;
import com.example.Lovable.repository.UserRepository;
import com.example.Lovable.security.AuthUtil;
import com.example.Lovable.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectMemberMapper projectMemberMapper;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    public Project getAccessibleProjectById(Long projectId,Long userId)
    {
        Project project=projectRepository.getProjectById(userId,projectId).orElseThrow(()->new ResourceNotFoundException("Project",projectId.toString()));
        return project;
    }

    @Override
    @PreAuthorize("@securityExpression.canViewMember(#projectId)")
    public List<MemberResponse> getProjectMembers(Long projectId) {
        Long userId= authUtil.getCurrentUserId();

        List<MemberResponse>projectMembers=new ArrayList<>();
        projectMembers.addAll(
                projectMemberRepository.findByIdProjectId(projectId)
                        .stream()
                        .map(projectMember -> projectMemberMapper.toMemberResponse(projectMember)).toList());

        return projectMembers;
    }

    @Override
    @PreAuthorize("@securityExpression.canManageMember(#projectId)")
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
        Long userId= authUtil.getCurrentUserId();

        Project project=projectRepository.getReferenceById(projectId);

        User invitee=userRepository.findByUsername(request.getUsername()).orElseThrow(()->new ResourceNotFoundException(
                "User", request.getUsername()));

        if(invitee.getId()==userId)
            throw new RuntimeException("Cannot invite yourself");

        ProjectMemberId projectMemberId=new ProjectMemberId(invitee.getId(),projectId);

        if(projectMemberRepository.existsById(projectMemberId))
            throw new RuntimeException("Cannot Invite Again , Already part of the Project");


        ProjectMember projectMember=ProjectMember.builder()
                .user(invitee)
                .project(project)
                .projectRole(request.getRole())
                .invitedAt(LocalDateTime.now())
                .build();

        projectMemberRepository.save(projectMember);
        return projectMemberMapper.toMemberResponse(projectMember);
    }

    @Override
    @PreAuthorize("@securityExpression.canManageMember(#projectId)")
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateRoleRequest request) {

        ProjectMemberId projectMemberId=new ProjectMemberId(memberId,projectId);

        ProjectMember projectMember=
                projectMemberRepository.findById(projectMemberId).orElseThrow(()->new ResourceNotFoundException(
                        "ProjectMember",projectMemberId));

        projectMember.setProjectRole(request.getRole());

        projectMemberRepository.save(projectMember);

        return projectMemberMapper.toMemberResponse(projectMember);
    }

    @Override
    @PreAuthorize("@securityExpression.canManageMember(#projectId)")
    public void deleteMember(Long projectId, Long memberId) {

        ProjectMemberId projectMemberId=new ProjectMemberId(memberId,projectId);

        if(!projectMemberRepository.existsById(projectMemberId))
            throw new ResourceNotFoundException("ProjectMember",projectMemberId);

        projectMemberRepository.deleteById(projectMemberId);

    }
}
