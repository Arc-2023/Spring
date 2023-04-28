package com.vueespring.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.vueespring.entity.FileEntity;
import com.vueespring.entity.NoteEnity;
import com.vueespring.entity.WebEntity.UserEntity;
import com.vueespring.service.NoteService;
import com.vueespring.service.IOService;
import com.vueespring.service.UserService;
import com.vueespring.utils.RedisOperator;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
public class NoteController implements Serializable {

    @Value("${minio.bucketName}")
    String bucketname;
    @Autowired
    public NoteService noteService;
    @Autowired
    IOService ioService;
    @Autowired
    UserService userService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MinioClient minioClient;
    @Autowired
    RedisOperator redisOperator;

    @GetMapping("/getImage/{filename}")
    public void getImage(@PathVariable("filename") String filename,
                         HttpServletResponse response,
                         HttpServletRequest request) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        String loginId =  StpUtil.getLoginIdAsString();
//        Query query = new Query(Criteria.where("filename").is(filename));
//        FileEntity entity = mongoTemplate.findOne(query, FileEntity.class);

        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucketname)
                .object(filename)
                .build();
        GetObjectResponse response1 = minioClient.getObject(args);
//        redisOperator.set(filename,args);
        response1.transferTo(response.getOutputStream());
    }

    @GetMapping("/getAllNotes")
    public SaResult getAllNotes() {
        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);
        List<NoteEnity> cards = noteService.getNotes(user.getUsername());
        if (cards == null) return SaResult.error("没找到你的笔记");
        return new SaResult().setCode(200).setData(cards);
    }

    @GetMapping("/getNote")
    @SaCheckLogin
    public SaResult getNote(String id,
                            HttpServletRequest request) {
        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);
        NoteEnity noteEnity = noteService.getNoteById(id);
        if (noteEnity.getCreater() != user.getUsername()) return SaResult.error("没有权限");
        if (noteEnity != null) {
            return new SaResult().setData(noteEnity)
                    .setCode(200);
        }
        return SaResult.error("没有找到笔记内容");
    }

    @PostMapping("/uploadImage")
    @SaCheckLogin
    public SaResult uploadImage(
            @RequestPart("file") MultipartFile file,
            HttpServletRequest request
    ) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String filename;
        try {
            filename = noteService.saveImg(file);
        } catch (Exception e) {
            return SaResult.error("上传失败");
        } ;
        return new SaResult()
                .setData(filename)
                .setCode(200);
    }

    @GetMapping("/delNote")
    @SaCheckLogin
    public SaResult delNote(String id,
                            HttpServletRequest request) {
        if (noteService.removeNoteById(id)) {
            return SaResult.ok("成功删除笔记");
        } else {
            return SaResult.error("删除失败");
        }
    }

    @PostMapping("/updateNote")
    @SaCheckLogin
    public SaResult updateNote(
            @RequestBody NoteEnity noteEnity) {
        Query query = new Query(Criteria
                .where("id")
                .is(noteEnity.getId()));
        Update update = new Update();
        update.set("id", noteEnity.getId());
        update.set("title", noteEnity.getTitle());
        update.set("creater", noteEnity.getCreater());
        update.set("intro", noteEnity.getIntro());
        update.set("content", noteEnity.getContent());
        mongoTemplate.updateFirst(query, update, NoteEnity.class);
        return SaResult.ok("成功更新");
    }

    @PostMapping("/addNote")
    @SaCheckLogin
    public SaResult addNote(@RequestBody(required = false) NoteEnity noteEnity) {
        Query query = new Query(Criteria
                .where("title")
                .is(noteEnity.getTitle())
                .and("creater")
                .is(noteEnity.getCreater()));
        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);
        NoteEnity note = mongoTemplate.findOne(query, NoteEnity.class);
        if (note == null) {
            note = new NoteEnity();
            note.setTitle(noteEnity.getTitle());
            note.setCreater(user.getUsername());
            note.setIntro(noteEnity.getIntro());
            mongoTemplate.insert(note);
        } else {
            return SaResult.ok("已经存在");
        }
        return SaResult.ok("添加成功");
    }

    @GetMapping("/deleteImage")
    @SaCheckLogin
    public SaResult deleteImage(@RequestParam String filename) {
        return ioService.delByFileName(filename);

    }
}
