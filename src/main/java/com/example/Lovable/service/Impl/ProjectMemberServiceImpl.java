package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.member.InviteMemberRequest;
import com.example.Lovable.dto.member.MemberResponse;
import com.example.Lovable.dto.member.UpdateRoleRequest;
import com.example.Lovable.entity.Project;
import com.example.Lovable.entity.ProjectMember;
import com.example.Lovable.entity.ProjectMemberId;
import com.example.Lovable.entity.User;
import com.example.Lovable.mapper.ProjectMemberMapper;
import com.example.Lovable.repository.ProjectMemberRepository;
import com.example.Lovable.repository.ProjectRepository;
import com.example.Lovable.repository.UserRepository;
import com.example.Lovable.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
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

    public Project getAccessibleProjectById(Long projectId,Long userId)
    {
        Project project=projectRepository.getProjectById(userId,projectId).orElseThrow();
        return project;
    }

    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {

        Project project=getAccessibleProjectById(projectId,userId);
        List<MemberResponse>projectMembers=new ArrayList<>();

        //Adding owner as the Member
        projectMembers.add(projectMemberMapper.toMemberResponse( project.getOwner()));

        projectMembers.addAll(
                projectMemberRepository.findByIdProjectId(projectId)
                        .stream()
                        .map(projectMember -> projectMemberMapper.toMemberResponse(projectMember)).toList());

        return projectMembers;
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
        Project project=getAccessibleProjectById(projectId,userId);

        if(project.getOwner().getId()!=userId)
            throw new RuntimeException("Action Not Allowed");

        User invitee=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("Invitee Does Not Exsist"));

        if(invitee.getId()==userId)
            throw new RuntimeException("Cannot invite yourself");

        ProjectMemberId projectMemberId=new ProjectMemberId(invitee.getId(),projectId);

        if(projectMemberRepository.existsById(projectMemberId))
        {
            System.out.println("******###########################################################################################*********");
            throw new RuntimeException("Cannot Invite Again , Already part of the Project");
        }


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
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateRoleRequest request, Long userId) {
        Project project=getAccessibleProjectById(projectId,userId);

        if(project.getOwner().getId()!=userId)
            throw new RuntimeException("Action Not Allowed");

        ProjectMemberId projectMemberId=new ProjectMemberId(memberId,projectId);

        ProjectMember projectMember=projectMemberRepository.findById(projectMemberId).orElseThrow();

        projectMember.setProjectRole(request.getRole());

        projectMemberRepository.save(projectMember);

        return projectMemberMapper.toMemberResponse(projectMember);
    }

    @Override
    public void deleteMember(Long projectId, Long memberId, Long userId) {
        Project project=getAccessibleProjectById(projectId,userId);

        if(project.getOwner().getId()!=userId)
            throw new RuntimeException("Action Not Allowed");

        ProjectMemberId projectMemberId=new ProjectMemberId(memberId,projectId);

        if(!projectMemberRepository.existsById(projectMemberId))
            throw new RuntimeException("Member does not exsist");

        projectMemberRepository.deleteById(projectMemberId);

    }
}
