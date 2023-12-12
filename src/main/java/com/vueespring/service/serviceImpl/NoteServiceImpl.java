package com.vueespring.service.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.vueespring.entity.FileEntity;
import com.vueespring.entity.NoteEnity;
import com.vueespring.entity.WebEntity.NoteCardEnity;
import com.vueespring.entity.WebEntity.UserEntity;
import com.vueespring.service.NoteService;
//import com.vueespring.utils.RedisOperator;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Cyk
 * @since 2022-10-27
 */
@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Value("${minio.bucketName}")
    String bucketName;
    @Autowired
    MinioClient minioClient;
//    @Autowired
//    RedisOperator redisOperator;

    @Override
    public String saveImg(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //匹配扩展名
//        String regex = "(?=\\.)(.+)$";
//        Matcher matcher = Pattern.compile(regex)
//                .matcher(Objects.requireNonNull(file.getOriginalFilename()));
        String fileid = IdUtil.fastSimpleUUID();
        String filename = fileid + "." + FileUtil.getSuffix(file.getOriginalFilename());
//            String filename = IdUtil.fastSimpleUUID() + matcher.group(1);
        HashMap<String, String> metadata = new HashMap<>();
        metadata.put("userid", StpUtil.getLoginIdAsString());
        PutObjectArgs putObjectArgs = PutObjectArgs
                .builder()
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(fileid)
                .bucket(bucketName)
                .userMetadata(metadata)
                .build();
        minioClient.putObject(putObjectArgs);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setUploaderid(StpUtil.getLoginIdAsString());
        fileEntity.setFilename(filename);
        mongoTemplate.insert(fileEntity);
        return filename;
    }

    @Override
    public List<NoteEnity> getNotes(String creater) {
        return mongoTemplate.find(new Query(Criteria.where("creater").is(creater)), NoteEnity.class);
    }

    @Override
    public NoteEnity getNoteById(String id) {
        return mongoTemplate.findById(id, NoteEnity.class);
    }

    @Override
    public Boolean removeNoteById(String id) {
        if (getNoteById(id) == null) return false;
        Query query = new Query(Criteria.where("id").is(id));

        Query q = new Query(Criteria.where("noteid").is(Objects.requireNonNull(mongoTemplate.findOne(query, NoteEnity.class)).getId()));
        Query userq = new Query(Criteria.where("id")
                .is(StpUtil.getLoginIdAsString()));

        UserEntity user = mongoTemplate.findOne(userq, UserEntity.class);
        NoteEnity note = mongoTemplate.findOne(query, NoteEnity.class);
        if (note != null && user != null && Objects.equals(user.getUsername(), note.getCreater())) {
            mongoTemplate.remove(query, NoteEnity.class);
            mongoTemplate.remove(q, NoteCardEnity.class);
            return true;
        } else return false;
    }

    @NotNull
    public NoteCardEnity setCardByNoteDefault(NoteEnity entity) {
        NoteCardEnity noteCardEnity = new NoteCardEnity();
        String str = getIntroByContent(entity.getContent());
//        log(str);
        noteCardEnity.setContent(str);
        noteCardEnity.setTitle(entity.getTitle());
        noteCardEnity.setCreator(entity.getCreater());
        noteCardEnity.setEditTime(entity.getCreatedTime());
        noteCardEnity.setCreatedTime(LocalDateTime.now());
        noteCardEnity.setView(0);
        noteCardEnity.setTag("default");
        noteCardEnity.setType("private");
        noteCardEnity.setNoteid(entity.getId());
        return noteCardEnity;
    }

    @Override
    public ArrayList<NoteCardEnity> getCardByUsername(String username) {

        Criteria criteria = new Criteria().orOperator(Criteria.where("creator")
                .is(username), Criteria.where("type").is("public"));
        Query q = new Query(criteria);
        return (ArrayList<NoteCardEnity>) mongoTemplate.find(q, NoteCardEnity.class);
    }

    @Override
    public ArrayList<NoteCardEnity> getPublicCards() {
        Query q = new Query(Criteria.where("type")
                .is("public"));
        return (ArrayList<NoteCardEnity>) mongoTemplate.find(q, NoteCardEnity.class);
    }

    @Override
    public String getIntroByContent(String content) {
        return content == null ? "null" : content.substring(0, content.length() < 100 ? content.length() : 50);
    }

    public Boolean increaseViewCount() {
        return true;
    }

}
