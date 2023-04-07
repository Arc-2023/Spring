package com.vueespring.service.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.vueespring.entity.FileEntity;
import com.vueespring.entity.WebEntity.UserEntity;
import com.vueespring.service.IOService;
import com.vueespring.service.UserService;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Data
@Component
@Service
public class IOServiceImpl implements IOService {
    @Value("${minio.bucketName}")
    String bucketname;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserService userService;
    @Autowired
    MinioClient minioClient;

    @Override
    public SaResult delByFileName(String filename) {
        Query query = new Query(Criteria.where("filename").is(filename));
        String id = StpUtil.getLoginIdAsString();
        UserEntity userById = userService.getUserById(id);

        String fileuserid = mongoTemplate.findOne(query, FileEntity.class).getUploaderid();
        if (userById.getId() != fileuserid) return SaResult.error("无删除权限");
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .object(filename)
                .bucket(bucketname)
                .build();
        try {
            minioClient.removeObject(args);
        } catch (Exception e){
            return SaResult.error("删除失败");
        }
        return SaResult.ok("删除成功");


    }

}
