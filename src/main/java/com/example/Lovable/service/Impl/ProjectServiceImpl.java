package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.project.ProjectRequest;
import com.example.Lovable.dto.project.ProjectResponse;
import com.example.Lovable.dto.project.ProjectSummaryResponse;
import com.example.Lovable.entity.Project;
import com.example.Lovable.entity.User;
import com.example.Lovable.repository.ProjectRepository;
import com.example.Lovable.repository.UserRepository;
import com.example.Lovable.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProjectSummaryResponse getUserProjects(Long userId) {
        return null;
    }

    @Override
    public ProjectResponse getProjectById(Long id, Long userId) {

        List<Project> user_project=projectRepository.findByUser(userId);
        Project project=(Project) user_project.stream().filter(project1 -> project1.getId()==id);

    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User user=userRepository.findById(userId).orElse(null);
        Project newProject= Project.builder()
                .name(request.getName())
                .user(user)
                .isPublic(false)
                .build();
        return modelMapper.map(projectRepository.save(newProject), ProjectResponse.class);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        return null;
    }

    @Override
    public void softDelete(Long id, Long userId) {

    }
}
