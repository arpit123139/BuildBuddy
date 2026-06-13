package com.example.Lovable.config;

import com.example.Lovable.dto.project.ProjectResponse;
import com.example.Lovable.dto.project.ProjectSummaryResponse;
import com.example.Lovable.entity.Project;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }
}
