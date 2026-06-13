package com.example.Lovable.mapper;

import com.example.Lovable.dto.auth.UserProfileResponse;
import com.example.Lovable.dto.project.ProjectResponse;
import com.example.Lovable.dto.project.ProjectSummaryResponse;
import com.example.Lovable.entity.Project;
import com.example.Lovable.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

        @Mapping(source = "owner",target = "user")
        ProjectResponse toProjectResponse(Project project);

        ProjectSummaryResponse toProjectSummaryResponse(Project project);

        UserProfileResponse toUserProfileResponse(User user);
}
