package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /** 与 server.servlet.context-path 一致，便于前端通过 /api 代理访问静态资源 */
    @Value("${server.servlet.context-path:}")
    private String servletContextPath;

    @PostMapping("/upload")
    public ApiResult<String> upload(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return ApiResult.fail("文件为空");
        }
        String orig = file.getOriginalFilename();
        String ext = orig != null && orig.contains(".") ? orig.substring(orig.lastIndexOf('.')) : "";
        String name = UUID.randomUUID().toString().replace("-", "") + ext;
        Path dir = Paths.get(uploadDir, "files");
        Files.createDirectories(dir);
        Path target = dir.resolve(name);
        file.transferTo(target.toFile());
        return ApiResult.ok(buildPublicFileUrl(name));
    }

    private String buildPublicFileUrl(String fileName) {
        String base = servletContextPath == null ? "" : servletContextPath.trim();
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/uploads/files/" + fileName;
    }
}
