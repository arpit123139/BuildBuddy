package com.example.Lovable.controller;

import com.example.Lovable.dto.member.InviteMemberRequest;
import com.example.Lovable.dto.member.MemberResponse;
import com.example.Lovable.dto.member.UpdateRoleRequest;
import com.example.Lovable.security.AuthUtil;
import com.example.Lovable.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;


    @GetMapping
    public ResponseEntity<List<MemberResponse>> getProjectMembers(@PathVariable Long projectId){
        return ResponseEntity.ok(projectMemberService.getProjectMembers(projectId));
    }

    @PostMapping
    public ResponseEntity<MemberResponse> inviteMember(@PathVariable Long projectId, @RequestBody @Valid InviteMemberRequest request){
        return ResponseEntity.ok(projectMemberService.inviteMember(projectId,request));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(@PathVariable Long projectId, @PathVariable Long memberId, @RequestBody @Valid  UpdateRoleRequest request){
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId,memberId,request));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long projectId, @PathVariable Long memberId){
        projectMemberService.deleteMember(projectId,memberId);
        return ResponseEntity.noContent().build();
    }
}
