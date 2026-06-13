package com.example.Lovable.service;

import com.example.Lovable.dto.project.ProjectRequest;
import com.example.Lovable.dto.project.ProjectResponse;
import com.example.Lovable.dto.project.ProjectSummaryResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectService {
     List<ProjectSummaryResponse> getUserProjects(Long userId);

     ProjectResponse getProjectById(Long id, Long userId);

      ProjectResponse createProject(ProjectRequest request, Long userId);

      ProjectResponse updateProject(Long id, ProjectRequest request, Long userId);

     void softDelete(Long id, Long userId);
}
