package com.example.Lovable.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProjectPermission {
    VIEW("project:view"),
    EDIT("project:edit"),
    DELETE("project:delete"),
    MANAGE_MEMBERS("ProjectMembers:manage_member"),
    VIEW_MEMBERS("ProjectMembers:view_member");

    private final String value;
}
