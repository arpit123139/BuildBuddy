package com.example.Lovable.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String stripeId;

    private Integer maxProjects;

    private Integer maxTokenPerDay;

    private Integer maxPreviews;

    private Boolean unlimitedAi;


    private Boolean  active;


}
