package com.vueespring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueespring.Scheduler.ThingScheduler;
import com.vueespring.entity.Thingstable;
import com.vueespring.entity.WebEntity.Item.ItemVOEntity;
import com.vueespring.entity.WebEntity.UserVoeEntity;
import com.vueespring.mapper.ThingstableMapper;
import com.vueespring.mapper.UserVoeTableMapper;
import com.vueespring.service.IThingstableService;
import com.vueespring.service.IUserVoeTableService;
import com.vueespring.shiro.JwtUtils;
import com.vueespring.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

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
    @Autowired
    public IUserVoeTableService iUserVoeTableService;
    @PostMapping("/additem")
    public JsonResult additem(@RequestBody ItemVOEntity itemVOEntity,
                              HttpServletRequest request){
        QueryWrapper<Thingstable> queryWrapper = new QueryWrapper<Thingstable>()
                .eq("id",itemVOEntity.getId());
        Thingstable thingt = iThingstableService.getOne(queryWrapper);
        if(thingt==null){
            return new JsonResult().error("Already existed");
        }else {
            String token = request.getHeader("Authentication");
            String userid = jwtUtils.getClaimByToken(token).getSubject();
            UserVoeEntity userinfo = iUserVoeTableService.getById(userid);
            if(userinfo==null){
                return new JsonResult().error("token错误或者无用户");
            }
            Thingstable thingstable = new Thingstable();
            thingstable.setName(itemVOEntity.getName());
            thingstable.setStartTime(itemVOEntity.getStart());
            thingstable.setEndTime(itemVOEntity.getEnd());
            thingstable.setMessage(itemVOEntity.getOthermsg().getMsg());
            thingstable.setType(itemVOEntity.getOthermsg().getType());
            thingstable.setTag(itemVOEntity.getOthermsg().getTag());
            thingstable.setUserid(Integer.parseInt(userid));
            thingstable.setAlertToken(itemVOEntity.getAlertToken());
            thingstable.setBy(userinfo.getUsername());
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
    @GetMapping("/getAllThing")
    public JsonResult getAllThing(){
        List<Thingstable> things = iThingstableService.list();
        return new JsonResult().ok(things);
    }
    @PostMapping("/changeitem")
    public JsonResult changeitem(@RequestBody ItemVOEntity item){
        Thingstable thing = iThingstableService.getById(item.getId());
        if(thing!=null){
            return new JsonResult().error("Don't Exist");
        }else {
            thing.setName(item.getName());
            thing.setStartTime(item.getStart());
            thing.setEndTime(item.getEnd());
            thing.setMessage(item.getOthermsg().getMsg());
            thing.setTag(item.getOthermsg().getTag());
            thing.setType(item.getOthermsg().getType());
            thing.setAlertToken(item.getAlertToken());
            if(thingstableMapper.insert(thing)==1){
                return new JsonResult().ok("insert successfully");
            }
            else {
                return new JsonResult().error("insert faild");
            }
        }
    }
}
