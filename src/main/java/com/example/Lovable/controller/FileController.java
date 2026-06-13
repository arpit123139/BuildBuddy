package com.example.Lovable.controller;

import com.example.Lovable.dto.project.FileContentResponse;
import com.example.Lovable.dto.project.FileNode;
import com.example.Lovable.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/projects/{projectId}/files")
public class FileController {

    private FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileNode>> getFileTree(@PathVariable Long projectId)
    {
        Long userId=1l;
        return ResponseEntity.ok(fileService.getFileTree(projectId,userId));
    }

    @GetMapping("/{*path}")
    public ResponseEntity<FileContentResponse> getFile(@PathVariable Long projectId, @PathVariable String path){
        Long userId=1l;
        return ResponseEntity.ok(fileService.getFileContent(projectId,path,userId));
    }

}
