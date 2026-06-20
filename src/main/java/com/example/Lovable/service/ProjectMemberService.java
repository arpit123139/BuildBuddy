package com.example.Lovable.service;

import com.example.Lovable.dto.member.InviteMemberRequest;
import com.example.Lovable.dto.member.MemberResponse;
import com.example.Lovable.dto.member.UpdateRoleRequest;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectMemberService {
     List<MemberResponse> getProjectMembers(Long projectId);

     MemberResponse inviteMember(Long projectId, InviteMemberRequest request);

     MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateRoleRequest request);

     void deleteMember(Long projectId, Long memberId);
}
