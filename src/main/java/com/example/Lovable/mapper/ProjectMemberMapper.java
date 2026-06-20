package com.example.Lovable.mapper;

import com.example.Lovable.dto.member.MemberResponse;
import com.example.Lovable.entity.ProjectMember;
import com.example.Lovable.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(source = "id",target = "userId")
    @Mapping(target = "projectRole", constant = "OWNER")
    MemberResponse toMemberResponse(User user);

    @Mapping(target = "userId",source = "user.id")
    @Mapping(target = "username",source = "user.username")
    MemberResponse toMemberResponse(ProjectMember projectMember);
}
