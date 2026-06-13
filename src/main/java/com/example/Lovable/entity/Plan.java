package com.example.Lovable.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    private Long id;

    private String name;

    private String stripeId;

    private Integer maxProjects;

    private Integer maxTokenPerDay;

    private Integer maxPreviews;

    private Boolean unlimitedAi;


    private Boolean  active;


}
