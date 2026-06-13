package com.example.Lovable.dto.subscription;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanResponse {

    private Long id;
    private String name;
    private Integer maxProjects;
    private Integer maxTokenPerDay;
    private Integer maxPreviews;
    private Boolean unlimitedAi;
    private Boolean  active;
}
