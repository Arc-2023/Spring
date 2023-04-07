package com.vueespring.controller;

import cn.dev33.satoken.util.SaResult;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LiveCheckController {
    @Value("${minio.bucketName}")
    private String bucketName;

    @GetMapping("/liveness")
    public SaResult liveness() {
        return SaResult.ok();
    }

    @GetMapping("/readiness")
    public SaResult readiness() {
        return SaResult.ok();
    }
}
