package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.project.ProjectRequest;
import com.example.Lovable.dto.project.ProjectResponse;
import com.example.Lovable.dto.project.ProjectSummaryResponse;
import com.example.Lovable.entity.Project;
import com.example.Lovable.entity.ProjectMember;
import com.example.Lovable.entity.ProjectMemberId;
import com.example.Lovable.entity.User;
import com.example.Lovable.enums.ProjectRole;
import com.example.Lovable.error.BadRequestException;
import com.example.Lovable.error.ResourceNotFoundException;
import com.example.Lovable.mapper.ProjectMapper;
import com.example.Lovable.repository.ProjectMemberRepository;
import com.example.Lovable.repository.ProjectRepository;
import com.example.Lovable.repository.UserRepository;
import com.example.Lovable.security.AuthUtil;
import com.example.Lovable.service.ProjectService;
import com.example.Lovable.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtil authUtil;
    private final SubscriptionService subscriptionService;

    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        Long userId=authUtil.getCurrentUserId();
        List<Project> user_project=projectRepository.findALLAccessibleBYUser(userId);
        List<ProjectSummaryResponse> projectSummaryResponses=
                user_project.stream().map(project -> projectMapper.toProjectSummaryResponse(project)).collect(Collectors.toList());

        return projectSummaryResponses;
    }

    @Override
    @PreAuthorize("@securityExpression.canViewProject(#id)")
    public ProjectResponse getProjectById(Long id) {
        Long userId=authUtil.getCurrentUserId();
        Project project=projectRepository.getProjectById(userId,id).orElseThrow(()->new ResourceNotFoundException("Project",id.toString()));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        Long userId=authUtil.getCurrentUserId();

        if(!subscriptionService.canCreateNewProject()){
            throw new BadRequestException("User cannot create a New Project , Plz upgrade your plan");
        }

        //TODO: Make a note
        User user=userRepository.getReferenceById(userId);

        Project newProject= Project.builder()
                .name(request.getName())
                .isPublic(false)
                .build();

        newProject=projectRepository.save(newProject);

        ProjectMemberId projectMemberId=new ProjectMemberId(userId, newProject.getId());

        ProjectMember projectMember=ProjectMember.builder()
                .user(user)
                .projectRole(ProjectRole.OWNER)
                .project(newProject)
                .invitedAt(LocalDateTime.now())
                .acceptedAt(LocalDateTime.now())
                .build();

        projectMemberRepository.save(projectMember);
        return projectMapper.toProjectResponse(newProject);
    }

    @Override
    @PreAuthorize("@securityExpression.canEditProject(#id)")
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Long userId=authUtil.getCurrentUserId();
        Project project=projectRepository.getProjectById(userId,id).orElseThrow(()->new ResourceNotFoundException("Project",id.toString()));

        //TODO: Only owner can update the Project
        project.setName(request.getName());

        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@securityExpression.canDelete(#id)")
    public void softDelete(Long id) {
        Long userId=authUtil.getCurrentUserId();
        Project project=projectRepository.getProjectById(userId,id).orElseThrow(()->new ResourceNotFoundException("Project",id.toString()));
        //TODO: Only owner can update the Project
        project.setDeletedAt(LocalDateTime.now());

    }
}
