package com.vueespring.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.vueespring.entity.WebEntity.UserEntity;
import com.vueespring.service.UserService;
import com.vueespring.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  前端控制器
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
    @RequestMapping("/login")
    public SaResult login(@RequestParam(value="username")String username,
                          @RequestParam(value="password")String password,
                          @RequestParam("remember") Boolean remember,
    HttpServletRequest request) {
        UserEntity user = userService.getuserByUsernamePassword(username,password);
        if(user==null){
            if(userService.getUserByUsername(username)!=null){
                return SaResult.error("密码错误");
            }
            else return SaResult.error("用户名错误");
        }
        String id = user.getId();
        if(remember){
            StpUtil.login(id,true);
        }else {
            StpUtil.login(id);
        }
        return new SaResult()
                .setCode(200)
                .setData(user)
                .setMsg("登录成功");
    }
    @GetMapping("/register")
    public SaResult register(@RequestParam String username,
                             @RequestParam String password,
                             HttpServletRequest request){
        UserEntity user = userService.getUserByUsername(username);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setPermission("user");
        if(user==null){
            UserEntity insert = mongoTemplate.insert(userEntity);
            if(insert!=null){
                return SaResult.ok("注册成功");
            }else {
                return SaResult.error("注册失败");
            }
        }
        else {
            return SaResult.error("用户名已存在");
        }
    }

    @GetMapping("/logout")
    public SaResult logout(
            HttpServletRequest request){
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
        update.set("alertToken",token);

        if(user==null){
            return SaResult.error("未找到用户，token无法修改");
        }

        user.setAlertToken(token);

        mongoTemplate.updateFirst(query,update,UserEntity.class);
        return SaResult.ok("update success");
    }
}
