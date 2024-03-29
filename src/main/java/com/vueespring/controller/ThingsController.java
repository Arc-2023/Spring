package com.vueespring.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.vueespring.entity.ThingEnity;
import com.vueespring.service.ThingService;
import com.vueespring.service.UpdateService;
import com.vueespring.service.UserService;
import com.vueespring.service.serviceImpl.QuartzServiceImpl;
import com.vueespring.entity.WebEntity.Item.Itemtity;
import com.vueespring.entity.WebEntity.UserEntity;
import com.vueespring.service.ThingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ThingsController {
    @Autowired
    public ThingsService thingsService;
    @Autowired
    public QuartzServiceImpl quartzservice;
    @Autowired
    public UserService userService;
    @Autowired
    public ThingService thingService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UpdateService updateService;

    @PostMapping("/addThing")
    @SaCheckLogin
    public SaResult addThing(@RequestBody Itemtity itemtity) {

        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);
        if(thingService.checkDupName(itemtity)) return SaResult.error("命名重复");
        assert user != null;
        if (user.getAlertToken() == null) return SaResult.error("AlertToken空");
        ThingEnity thing = thingService.getThingByVoe(itemtity, user.getId(), user);
        mongoTemplate.insert(thing);
        return SaResult.ok("插入Thing成功");
    }

    @GetMapping("/refreshThings")
    @SaCheckLogin
    public SaResult refreshThings() throws Exception {
        UserEntity user = userService.getUserById(StpUtil.getLoginIdAsString());
        Query query = new Query(Criteria.where("creater").is(user.getUsername()));
        List<ThingEnity> thingEnities = mongoTemplate.find(query, ThingEnity.class);
        if(thingEnities.isEmpty()) return SaResult.error("暂无事务记录");
        quartzservice.startThings(thingEnities
                .parallelStream()
                .filter(item -> thingService.checkAndSetStatus(item).equals("Running"))
                .collect(Collectors.toList()));
        return new SaResult().setData(thingEnities)
                .setCode(200)
                .setMsg("成功刷新");
    }

    @PostMapping("/updateThing")
    @SaCheckLogin
    public SaResult updateThing(@RequestBody Itemtity item) throws Exception {
        Query query = new Query(Criteria.where("id").is(item.getId()));
        Update update = updateService.getUpdateByItem(item);
        ThingEnity thing = mongoTemplate.findOne(query, ThingEnity.class);
        if (thing == null) {
            return SaResult.error("Don't Exist");
        } else {
            if(thingService.checkDupName(item)) return SaResult.error("命名重复");
            List<ThingEnity> listpre = new ArrayList<ThingEnity>();
            listpre.add(thing);
            quartzservice.delthings(listpre);
            thingsService.setThingByItem(item, thing);

            mongoTemplate.updateFirst(query, update, ThingEnity.class);
            if (thing.getStatus().equals("Running")) {
                List<ThingEnity> listnow = new ArrayList<ThingEnity>();
                listnow.add(thing);
                quartzservice.startThings(listnow);
            }
            return SaResult.ok("insert successfully");
        }
    }

    @GetMapping("/delItem")
    @SaCheckLogin
    public SaResult delItem(@RequestParam("id") String id) {

        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);

        Query query = new Query(Criteria.where("id").is(id));

        ThingEnity thing = mongoTemplate.findOne(query, ThingEnity.class);


        if (thing == null || !Objects.equals(user.getUsername(), thing.getCreater())) {
            return SaResult.error("Don't Exist");
        } else {
            mongoTemplate.remove(query, ThingEnity.class);
            return SaResult.ok("del successfully");
        }
    }

    @GetMapping("/startItem")
    @SaCheckLogin
    public SaResult startItem(String id) throws Exception {
        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);
        Query query = new Query(Criteria.where("id").is(id));
        ThingEnity thing = mongoTemplate.findOne(query, ThingEnity.class);
        if (thing == null || user == null) return SaResult.error("无匹配信息");
        if (!user.getUsername().equals(thing.getCreater())) {
            log.info(user.getUsername());
            log.info(thing.getCreater());
            return SaResult.error("鉴权失败");
        } else {
            LocalDateTime now = LocalDateTime.now();
            if (thing.getStatus() == "Running") {
                return SaResult.error("Already Running");
            } else if (thing.getEndTime().isBefore(now)) {
                thing.setStatus("Expired");
                mongoTemplate.updateFirst(query, updateService.updateThingEnity(thing), ThingEnity.class);
                return SaResult.error("Expired");
            } else {
                thing.setStatus("Running");
                List<ThingEnity> list = new ArrayList<ThingEnity>();
                list.add(thing);
                if (quartzservice.startThings(list)) {
                    mongoTemplate.updateFirst(query, updateService.updateThingEnity(thing), ThingEnity.class);
                    return SaResult.ok("started");
                } else {
                    return SaResult.error("faild to start");
                }
            }
        }
    }

    @GetMapping("/pauseItem")
    @SaCheckLogin
    public SaResult pauseItem(@RequestParam("id") String id) throws Exception {
        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);
        Query query = new Query(Criteria.where("id").is(id));
        ThingEnity thing = mongoTemplate.findOne(query, ThingEnity.class);
        if (thing == null) {
            return SaResult.error("无此事务");
        } else if (Objects.equals(user.getUsername(), thing.getCreater())) {
            List<ThingEnity> things = new ArrayList<>();
            things.add(thing);
            thing.setStatus("Pause");
            quartzservice.pausethings(things);
            things.forEach(item -> {
                mongoTemplate.updateFirst(query, updateService.updateThingEnity(item), ThingEnity.class);
            });
            return SaResult.ok("Pause");
        } else return SaResult.error("无权限");
    }

    @GetMapping("/initStart")
    @SaCheckLogin
    public SaResult initStart() throws Exception {
        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);

        Query query = new Query(Criteria.where("creater").is(user.getUsername()));
        List<ThingEnity> things = mongoTemplate.find(query, ThingEnity.class);
        quartzservice.initstart();
        quartzservice.startThings(things.stream()
                .filter(item -> thingService.checkAndSetStatus(item)
                        .equals("Running"))
                .collect(Collectors.toList()));
        return SaResult.ok("init successfully");
    }

}
