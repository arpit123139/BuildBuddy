package com.example.Lovable.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectFile {

     Long id;

     Project project;

     String path;

     String minioObjectKey;

     LocalDateTime createdAt= LocalDateTime.now();

     LocalDateTime updatedAt=LocalDateTime.now();

     User createdBy;

     User updatedBy;

}
