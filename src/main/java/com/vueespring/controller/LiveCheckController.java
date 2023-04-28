package com.vueespring.controller;

import cn.dev33.satoken.util.SaResult;
import com.vueespring.entity.WebEntity.UserEntity;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class LiveCheckController {
    @Autowired
    WebClient webClient;
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
