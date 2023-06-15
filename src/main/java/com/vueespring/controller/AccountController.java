package com.vueespring.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.mongodb.client.result.UpdateResult;
import com.vueespring.entity.ThingEnity;
import com.vueespring.entity.WebEntity.UserEntity;
import com.vueespring.service.QuartzService;
import com.vueespring.service.ThingService;
import com.vueespring.service.ThingsService;
import com.vueespring.service.UserService;
import com.vueespring.utils.JsonResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Cyk
 * @since 2022-10-17
 */
@RestController
@Slf4j
@CrossOrigin
public class AccountController {
    @Autowired
    UserService userService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    QuartzService quartzService;
    @Autowired
    ThingService thingService;

    @RequestMapping("/login")
    public SaResult login(@RequestParam(value = "username") String username,
                          @RequestParam(value = "password") String password,
                          @RequestParam("remember") Boolean remember,
                          HttpServletRequest request) {
        UserEntity user = userService.getuserByUsernamePassword(username, password);
        if (user == null) {
            if (userService.getUserByUsername(username) != null) {
                return SaResult.error("密码错误");
            } else return SaResult.error("用户名错误");
        }
        String id = user.getId();
        StpUtil.login(id, remember);
        return new SaResult()
                .setCode(200)
                .setData(user)
                .setMsg("登录成功");
    }

    @GetMapping("/register")
    public SaResult register(@RequestParam String username,
                             @RequestParam String password,
                             HttpServletRequest request) {
        UserEntity user = userService.getUserByUsername(username);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setPermission("user");
        if (user == null) {
            UserEntity insert = mongoTemplate.insert(userEntity);
            return SaResult.ok("注册成功");
        } else {
            return SaResult.error("用户名已存在");
        }
    }

    @GetMapping("/logout")
    public SaResult logout(
            HttpServletRequest request) {
        StpUtil.logout();
        return SaResult.ok("已登出");
    }

    @GetMapping("/setAlertToken")
    public SaResult setToken(
            @RequestParam("alertToken") String token,
            HttpServletRequest request) {
        String loginId = StpUtil.getLoginIdAsString();
        UserEntity user = userService.getUserById(loginId);
        Query query = new Query(Criteria.where("id")
                .is(user.getId()));
        Update update = new Update();
        update.set("alertToken", token);
        Query query1 = new Query(Criteria.where("userid")
                .is(user.getId()));

        user.setAlertToken(token);

        mongoTemplate.updateFirst(query, update, UserEntity.class);
        mongoTemplate.updateFirst(query1, update, ThingEnity.class);
        List<ThingEnity> list = mongoTemplate.find(query1, ThingEnity.class);
        try {
            quartzService.startThings(list.stream().filter(item -> thingService.checkAndSetStatus(item).equals("Running")).collect(Collectors.toList()));
        } catch (Exception e) {
            return SaResult.error("token已修改,Things未重新启动，你可以刷新或者重新添加事务");
        }
        return SaResult.ok("token已修改,Things已重新启动");
    }

    @PostMapping(value = "/setUserIcon")
    public SaResult setUserIcon(@RequestBody Map<String, String> payload) {
        String loginId = StpUtil.getLoginIdAsString();
        Query query = new Query(Criteria.where("id")
                .is(loginId));
        UserEntity one = mongoTemplate.findOne(query, UserEntity.class);
        if (one != null) {
            Update update = new Update();
            ;
            update.set("avatar", payload.get("url"));
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, UserEntity.class);
            if (updateResult.wasAcknowledged()) return SaResult.ok("更新成功");
        } else {
            return SaResult.error("用户信息错误");
        }
        return SaResult.error("更新失败");
    }
}
