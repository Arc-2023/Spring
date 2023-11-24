package com.vueespring.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.vueespring.entity.ProgressEntity;
import com.vueespring.entity.WebEntity.UserEntity;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class ProgressController {
    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping("/addCard")
    @SaCheckRole
    public SaResult addCard(@RequestBody ProgressEntity entity) {
        Query q = new Query(Criteria.where("title").is(entity.getTitle()));
        if (mongoTemplate.findOne(q, ProgressEntity.class) != null) return SaResult.error("不允许重复添加");
        // 临时权限验证
        Query qq = new Query(Criteria.where("id").is(StpUtil.getLoginIdAsString()));
        UserEntity one = mongoTemplate.findOne(qq, UserEntity.class);

        if (one == null) return SaResult.error("无此人");
        else if (!Objects.equals(one.getPermission(), "SuperUser")) return SaResult.error("认证失败,目前仅作者可修改");
        entity.setProgress(0);
        entity.setStatus(false);
        entity.setCreater(one.getUsername());
        mongoTemplate.insert(entity);
        return SaResult.ok("添加完毕");
    }

    @PostMapping("/updateCard")
    @SaCheckLogin
    public SaResult updateCard(@RequestBody ProgressEntity entity) {
        Query q = new Query(Criteria.where("id").is(StpUtil.getLoginIdAsString()));
        Query qq = new Query(Criteria.where("id").is(entity.getId()));
        ProgressEntity card = mongoTemplate.findOne(qq, ProgressEntity.class);
        UserEntity one = mongoTemplate.findOne(q, UserEntity.class);
        if (card == null || one == null) return SaResult.error("查找失败,服务器无记录");
        if (!Objects.equals(card.getCreater(), one.getUsername())) return SaResult.error("你不是作者，鉴权失败");
        if (entity.getStatus() && entity.getProgress() != 100) return SaResult.error("内容有错误");
        Update update = new Update();
        update.set("title", entity.getTitle());
        update.set("intro", entity.getIntro());
        update.set("status", entity.getStatus());
        update.set("progress", entity.getProgress());
        mongoTemplate.updateFirst(qq, update, ProgressEntity.class);
        return SaResult.ok("更新成功");

    }

    @GetMapping("/delCard")
    @SaCheckLogin
    public SaResult delCard(@RequestParam("id") String id) {
        Query q = new Query(Criteria.where("id").is(StpUtil.getLoginIdAsString()));
        Query qq = new Query(Criteria.where("id").is(id));
        ProgressEntity card = mongoTemplate.findOne(qq, ProgressEntity.class);
        if (card == null) return SaResult.error("找不到card");
        UserEntity one = mongoTemplate.findOne(q, UserEntity.class);
        if (one == null) return SaResult.error();
        if (!Objects.equals(one.getUsername(), card.getCreater())) return SaResult.error("鉴权失败");
        mongoTemplate.remove(qq, ProgressEntity.class);
        return SaResult.ok("删除成功");
    }

    @GetMapping("/getCards")
    @SaCheckLogin
    public SaResult getCards() {
        List<ProgressEntity> all = mongoTemplate.findAll(ProgressEntity.class);
        return SaResult.ok("查询完毕")
                .setData(all);
    }
}
