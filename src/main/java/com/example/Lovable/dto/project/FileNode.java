package com.example.Lovable.dto.project;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileNode {

    String path;

    LocalDateTime modifiedAt;

    Long size;
    String type;
}
