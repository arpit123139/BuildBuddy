package com.example.Lovable.entity;

import com.example.Lovable.enums.PreviewStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Preview {

    Long id;
    Project project;

    String namespace;

    String podName;

    String previewUrl;

    LocalDateTime starttedAt;

    LocalDateTime createdAt;

    LocalDateTime terminatedAt;

    PreviewStatus status;


}
