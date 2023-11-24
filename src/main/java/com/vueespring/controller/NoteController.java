package com.vueespring.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.vueespring.entity.NoteEnity;
import com.vueespring.entity.WebEntity.NoteCardEnity;
import com.vueespring.entity.WebEntity.UserEntity;
import com.vueespring.service.NoteService;
import com.vueespring.service.IOService;
import com.vueespring.service.UserService;
import com.vueespring.utils.CurrencyLimiter;
import com.vueespring.utils.RedisOperator;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static cn.hutool.core.lang.Console.log;

@RestController
@Slf4j
public class NoteController implements Serializable {

    @Value("${minio.bucketName}")
    String bucketname;
    @Autowired
    NoteService noteService;
    @Autowired
    IOService ioService;
    @Autowired
    UserService userService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MinioClient minioClient;
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    CurrencyLimiter limiter;

    @GetMapping("/getImage/{filename}")
    public void getImage(@PathVariable("filename") String filename,
                         HttpServletResponse response,
                         HttpServletRequest request) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ExecutionException, InterruptedException {
//        String loginId =  StpUtil.getLoginIdAsString();
//        Query query = new Query(Criteria.where("filename").is(filename));
//        FileEntity entity = mongoTemplate.findOne(query, FileEntity.class);
        filename = FileUtil.getPrefix(filename);
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucketname).object(filename).build();
        try (GetObjectResponse object = minioClient.getObject(args)) {
            IoUtil.copy(object, response.getOutputStream());
        }
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
        }
        ;
        return new SaResult()
                .setData(filename)
                .setCode(200);
    }

    @GetMapping("/getNote")
    public SaResult getNote(String id) {
        Query q = new Query(Criteria.where("id")
                .is(id));
        Query qq = new Query(Criteria.where("noteid")
                .is(id));
        NoteEnity note = mongoTemplate.findOne(q, NoteEnity.class);
        NoteCardEnity card = mongoTemplate.findOne(qq, NoteCardEnity.class);
        Update update = new Update();
        if (card == null) return SaResult.error("无此Card");
        update.set("view", card.getView() + 1);
        if (note == null) return SaResult.error("无此Note");
        else{
            if (note.getType().equals("public")) {
                mongoTemplate.updateFirst(qq,update,NoteCardEnity.class);
                return SaResult.data(note).setMsg("获取成功");
            }
            else if (!StpUtil.isLogin()) return SaResult.error("未登录，无法获取笔记");

            else {
                String username = userService.getUserById(StpUtil.getLoginIdAsString()).getUsername();
                if (!Objects.equals(username, note.getCreater())) {
                    return SaResult.error("无法访问其他人的笔记");
                } else {
                    mongoTemplate.updateFirst(qq,update,NoteCardEnity.class);
                    return SaResult.data(note).setMsg("获取成功");
                }
            }
        }

    }

    @GetMapping("/getNoteCards")
    public SaResult getNoteCards() {
        ArrayList<NoteCardEnity> publicCards = noteService.getPublicCards();
        if (!StpUtil.isLogin()) {
            return SaResult.data(publicCards).setMsg("未登录");
        }
        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);
        ArrayList<NoteCardEnity> cardByUsername = noteService.getCardByUsername(user.getUsername());
        return new SaResult().setCode(200).setData(cardByUsername);
    }


    @GetMapping("/delNote")
    @SaCheckLogin
    public SaResult delNote(String id, HttpServletRequest request) {
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
        Query qq = new Query(Criteria.where("username").is(userService.getUserById(StpUtil.getLoginIdAsString()).getUsername()));
        Query qqq = new Query(Criteria.where("id").is(noteEnity.getId()));
        UserEntity one = mongoTemplate.findOne(qq, UserEntity.class);
        NoteEnity one1 = mongoTemplate.findOne(qqq, NoteEnity.class);
        if (one == null || one1 == null) {
            return SaResult.error("查询错误，检查id");
        } else if (!one.getUsername().equals(one1.getCreater())) {
            return SaResult.error("鉴权失败");
        }
        Query query = new Query(Criteria
                .where("id")
                .is(noteEnity.getId()));
        Query q = new Query(Criteria
                .where("noteid")
                .is(noteEnity.getId()));
        Update update = new Update();
        update.set("title", noteEnity.getTitle());
        update.set("creater", noteEnity.getCreater());
        update.set("intro", noteEnity.getIntro());
        update.set("content", noteEnity.getContent());
        update.set("tag", noteEnity.getTag());
        update.set("type", noteEnity.getType());
        Update up = new Update();

        NoteCardEnity card = noteService.setCardByNoteDefault(noteEnity);

        up.set("title", noteEnity.getTitle());
        up.set("editTime", LocalDateTime.now());
        up.set("tag", noteEnity.getTag());
        up.set("type", noteEnity.getType());
        up.set("content", noteService.getIntroByContent(noteEnity.getContent()));

        mongoTemplate.updateFirst(query, update, NoteEnity.class);
        mongoTemplate.updateFirst(q, up, NoteCardEnity.class);

        return SaResult.ok("成功更新");
    }

    @PostMapping("/addNote")
    @SaCheckLogin
    public SaResult addNote(@RequestBody(required = false) NoteEnity noteEnity) {
        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);
        Query query = new Query(Criteria
                .where("title")
                .is(noteEnity.getTitle())
                .and("creater")
                .is(user.getUsername()));
        Query q = new Query(Criteria.where("title")
                .is(noteEnity.getTitle())
                .and("username")
                .is(user.getUsername()));
        NoteEnity note = mongoTemplate.findOne(query, NoteEnity.class);
        if (note == null) {
            note = new NoteEnity();
            note.setTitle(noteEnity.getTitle());
            note.setCreater(user.getUsername());
            note.setIntro(noteEnity.getIntro());
            note.setCreatedTime(LocalDateTime.now());
            note.setLastedittime(LocalDateTime.now());
            note.setContent("null");
            note.setType("private");
            note.setTag("Default");
            mongoTemplate.insert(note);
            note = mongoTemplate.findOne(query, NoteEnity.class);
            NoteCardEnity entity = noteService.setCardByNoteDefault(note);
            mongoTemplate.insert(entity);
        } else {
            return SaResult.error("已经存在");
        }
        return SaResult.ok("添加成功");
    }

    @GetMapping("/deleteImage")
    @SaCheckLogin
    public SaResult deleteImage(@RequestParam String filename) {
        return ioService.delByFileName(filename);

    }

    //    @GetMapping("/getNoteList")
//    public SaResult getNoteList() {
//        if (!StpUtil.isLogin()) {
//            Query query = new Query(Criteria
//                    .where("type")
//                    .is("public"));
//            mongoTemplate.find(query,)
//
//        }
//
//    }
    @GetMapping("/generCards")
    public SaResult generCards() {
//        Query query = new Query();
//        List<NoteEnity> noteEnities = mongoTemplate.find(query, NoteEnity.class);
//        log(noteEnities);
//        ArrayList<NoteCardEnity> noteCardEnities = new ArrayList<>();
//
//        for (NoteEnity entity : noteEnities) {
//            NoteCardEnity noteCardEnity = noteService.setCardByNoteDefault(entity);
//            noteCardEnities.add(noteCardEnity);
//        }

        Query q = new Query();
        Update up = new Update();
        up.set("createdTime", LocalDateTime.now());
        up.set("lastedittime", LocalDateTime.now());
        up.set("type", "private");
        up.set("tag", "Default");
//        mongoTemplate.insert(noteCardEnities, NoteCardEnity.class);
        mongoTemplate.updateMulti(q, up, "noteenitys");
        return SaResult.data(mongoTemplate.find(q, NoteEnity.class));
    }


}
