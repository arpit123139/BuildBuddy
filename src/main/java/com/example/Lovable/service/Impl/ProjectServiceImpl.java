package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.project.ProjectRequest;
import com.example.Lovable.dto.project.ProjectResponse;
import com.example.Lovable.dto.project.ProjectSummaryResponse;
import com.example.Lovable.entity.Project;
import com.example.Lovable.entity.ProjectMember;
import com.example.Lovable.entity.ProjectMemberId;
import com.example.Lovable.entity.User;
import com.example.Lovable.enums.ProjectRole;
import com.example.Lovable.error.ResourceNotFoundException;
import com.example.Lovable.mapper.ProjectMapper;
import com.example.Lovable.repository.ProjectMemberRepository;
import com.example.Lovable.repository.ProjectRepository;
import com.example.Lovable.repository.UserRepository;
import com.example.Lovable.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {

        List<Project> user_project=projectRepository.findALLAccessibleBYUser(userId);
        List<ProjectSummaryResponse> projectSummaryResponses=
                user_project.stream().map(project -> projectMapper.toProjectSummaryResponse(project)).collect(Collectors.toList());

        return projectSummaryResponses;
    }

    @Override
    public ProjectResponse getProjectById(Long id, Long userId) {

        Project project=projectRepository.getProjectById(userId,id).orElseThrow(()->new ResourceNotFoundException("Project",id.toString()));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User",userId.toString()));
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
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        Project project=projectRepository.getProjectById(userId,id).orElseThrow(()->new ResourceNotFoundException("Project",id.toString()));

        //TODO: Only owner can update the Project
        project.setName(request.getName());

        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id, Long userId) {
        Project project=projectRepository.getProjectById(userId,id).orElseThrow(()->new ResourceNotFoundException("Project",id.toString()));
        //TODO: Only owner can update the Project
        project.setDeletedAt(LocalDateTime.now());

    }
}
