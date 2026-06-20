package com.example.Lovable.service;

import com.example.Lovable.dto.project.ProjectRequest;
import com.example.Lovable.dto.project.ProjectResponse;
import com.example.Lovable.dto.project.ProjectSummaryResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectService {
     List<ProjectSummaryResponse> getUserProjects();

     ProjectResponse getProjectById(Long id );

      ProjectResponse createProject(ProjectRequest request );

      ProjectResponse updateProject(Long id, ProjectRequest request );

     void softDelete(Long id);
}
