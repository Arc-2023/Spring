package com.vueespring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueespring.Scheduler.ThingScheduler;
import com.vueespring.entity.Thingstable;
import com.vueespring.entity.WebEntity.Item.ItemVOEntity;
import com.vueespring.mapper.ThingstableMapper;
import com.vueespring.service.IThingstableService;
import com.vueespring.shiro.JwtUtils;
import com.vueespring.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

@RestController
public class ThingsController {
    @Autowired
    public ThingstableMapper thingstableMapper;
    @Autowired
    public IThingstableService iThingstableService;
    @Autowired
    public JwtUtils jwtUtils;
    @Autowired
    public ThingScheduler thingScheduler;
    @PostMapping("/additem")
    public JsonResult additem(@RequestBody ItemVOEntity itemVOEntity,
                              HttpServletRequest request){
        QueryWrapper<Thingstable> queryWrapper = new QueryWrapper<Thingstable>()
                .eq("id",itemVOEntity.getId());
        if(iThingstableService.count(queryWrapper)>0){
            return new JsonResult().error("Already existed");
        }else {
            String token = request.getHeader("Authentication");
            String userid = jwtUtils.getClaimByToken(token).getSubject();
            System.out.println(userid);
            Thingstable thingstable = new Thingstable();
            thingstable.setName(itemVOEntity.getName());
            thingstable.setStartTime(itemVOEntity.getStart());
            thingstable.setEndTime(itemVOEntity.getEnd());
            thingstable.setMessage(itemVOEntity.getOthermsg().getMsg());
            thingstable.setType(itemVOEntity.getOthermsg().getType());
            thingstable.setTag(itemVOEntity.getOthermsg().getTag());
            thingstable.setUserid(Integer.parseInt(userid));
            if(thingstableMapper.insert(thingstable)>0){
                return new JsonResult().ok("Submit Successfully");
            }else {
                return new JsonResult().error("Submit Faild");
            }
        }
    }
    @GetMapping("/start")
    public JsonResult start(@PathParam("username") String username){
        try {
            thingScheduler.init(username);
            return new JsonResult().ok("ok");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
