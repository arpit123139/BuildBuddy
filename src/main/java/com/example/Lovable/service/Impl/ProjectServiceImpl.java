package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.project.ProjectRequest;
import com.example.Lovable.dto.project.ProjectResponse;
import com.example.Lovable.dto.project.ProjectSummaryResponse;
import com.example.Lovable.entity.Project;
import com.example.Lovable.entity.User;
import com.example.Lovable.mapper.ProjectMapper;
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
    private final  ModelMapper modelMapper;

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {

        List<Project> user_project=projectRepository.findALLAccessibleBYUser(userId);
        List<ProjectSummaryResponse> projectSummaryResponses=
                user_project.stream().map(project -> projectMapper.toProjectSummaryResponse(project)).collect(Collectors.toList());

        return projectSummaryResponses;
    }

    @Override
    public ProjectResponse getProjectById(Long id, Long userId) {

        Project project=projectRepository.getProjectById(userId,id).orElseThrow();
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User user=userRepository.findById(userId).orElse(null);
        Project newProject= Project.builder()
                .name(request.getName())
                .owner(user)
                .isPublic(false)
                .build();

        Project saved_project=projectRepository.save(newProject);
        return projectMapper.toProjectResponse(saved_project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        Project project=projectRepository.getProjectById(userId,id).orElseThrow();
        if(project.getOwner().getId()!=userId)   // Just a Check
            throw new RuntimeException("You are not Allowed to Update the Project");
        project.setName(request.getName());

        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id, Long userId) {
        Project project=projectRepository.getProjectById(userId,id).orElseThrow();
        if(project.getOwner().getId()!=userId)   // Just a Check
           throw new RuntimeException("You are not Allowed to delete the Project");
        project.setDeletedAt(LocalDateTime.now());

    }
}
