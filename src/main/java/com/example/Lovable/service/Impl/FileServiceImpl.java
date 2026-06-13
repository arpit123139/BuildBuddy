package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.project.FileContentResponse;
import com.example.Lovable.dto.project.FileNode;
import com.example.Lovable.service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public List<FileNode> getFileTree(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path, Long userId) {
        return null;
    }
}
