package com.vueespring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueespring.entity.WebEntity.UserVoeEntity;
import com.vueespring.mapper.UserVoeTableMapper;
import com.vueespring.service.IUserVoeTableService;
import com.vueespring.service.QuartzService;
import com.vueespring.shiro.JwtUtils;
import com.vueespring.utils.JsonResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import cn.hutool.crypto.SecureUtil;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
public class AccountController {
    @Autowired
    IUserVoeTableService iUserVoeTableService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserVoeTableMapper userVoeTableMapper;
    @Autowired
    QuartzService quartzService;
    @RequestMapping("/login")
    public JsonResult login(@RequestParam(value="username")String username,
                            @RequestParam(value="password")String password,
                            HttpServletResponse response) {
        QueryWrapper<UserVoeEntity> wrapper = new QueryWrapper<UserVoeEntity>().eq("username",username);
        log.debug(username);
        log.debug(password);
        UserVoeEntity voeEntity = iUserVoeTableService.getOne(wrapper);
        log.debug(voeEntity.toString());
        if(!voeEntity.getPassword().equals(SecureUtil.md5(password))){
            return new JsonResult().error("密码错误");
        }
        else {
            String token = jwtUtils.generateToken(voeEntity.getId());
            LocalDateTime time = LocalDateTime.now().plusSeconds(60*60*48);
            response.setCharacterEncoding("utf-8");
            response.setHeader("Token",token);
            response.setHeader("Access-Control-Expose-Headers", "token");
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("username",voeEntity.getUsername());
            map.put("message","Login Successfully");
            map.put("role",voeEntity.getPermission());
            map.put("nextexpiretime",time);
            return new JsonResult().ok(map);
        }
    }
    @GetMapping("/register")
    public JsonResult register(@RequestParam String username,
                               @RequestParam String password,
                               HttpServletResponse response){
        QueryWrapper<UserVoeEntity> wrapper = new QueryWrapper<UserVoeEntity>().eq("username",username);
        if(iUserVoeTableService.count(wrapper)>0){
            return new JsonResult().error("已有账号，可登陆");
        }else {
            if(iUserVoeTableService.save(new UserVoeEntity(null,username,SecureUtil.md5(password),"user"))){
                return new JsonResult().ok("注册成功");
            }else {
                return new JsonResult().error("注册失败");
            }
        }
    }
    @GetMapping("/logout")
    public JsonResult logout(){
        SecurityUtils.getSubject().logout();
        return new JsonResult().ok("退出成功");
    }
}
