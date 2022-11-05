package com.vueespring.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.x.protobuf.MysqlxExpr;
import com.vueespring.service.QuartzService;
import com.vueespring.service.ThingService;
import com.vueespring.service.serviceImpl.QuartzServiceImpl;
import com.vueespring.entity.Thingstable;
import com.vueespring.entity.WebEntity.Item.ItemVOEntity;
import com.vueespring.entity.WebEntity.UserVoeEntity;
import com.vueespring.mapper.ThingstableMapper;
import com.vueespring.service.IThingstableService;
import com.vueespring.service.IUserVoeTableService;
import com.vueespring.shiro.JwtToken;
import com.vueespring.shiro.JwtUtils;
import com.vueespring.utils.JsonResult;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ThingsController {
    @Autowired
    public ThingstableMapper thingstableMapper;
    @Autowired
    public IThingstableService iThingstableService;
    @Autowired
    public JwtUtils jwtUtils;
    @Autowired
    public QuartzServiceImpl quartzservice;
    @Autowired
    public IUserVoeTableService iUserVoeTableService;
    @Autowired
    public ThingService thingService;
    @PostMapping("/additem")
    public JsonResult additem(@RequestBody ItemVOEntity itemVOEntity,
                              HttpServletRequest request){
        QueryWrapper<Thingstable> queryWrapper = new QueryWrapper<Thingstable>()
                .eq("id",itemVOEntity.getId());
        Integer count = iThingstableService.count(queryWrapper);
        if(count>0){
            return new JsonResult().error("Already existed");
        }else {
            String token = request.getHeader("token");
            Claims claimByToken = jwtUtils.getClaimByToken(token);
            String userid = claimByToken.getSubject();
            UserVoeEntity userinfo = iUserVoeTableService.getById(userid);
            if(userinfo==null){
                return new JsonResult().error("token错误或者无用户");
            }
            Thingstable thingstable = new Thingstable();
            thingstable.setName(itemVOEntity.getName());
            thingstable.setStartTime(itemVOEntity.getStartTime());
            thingstable.setEndTime(itemVOEntity.getEndTime());
            thingstable.setMessage(itemVOEntity.getMessage());
            thingstable.setType(itemVOEntity.getType());
            thingstable.setTag(itemVOEntity.getTag());
            thingstable.setUserid(Integer.parseInt(userid));
            thingstable.setCreater(userinfo.getUsername());
            thingstable.setAlertToken(itemVOEntity.getAlertToken());
            thingstable.setStatus("Pause");
            if(thingstableMapper.insert(thingstable) > 0){
                return new JsonResult().ok("Submit Successfully");
            }else {
                return new JsonResult().error("Submit Faild");
            }
        }
    }
    @GetMapping("/refreshthings")
    @RequiresAuthentication
    public JsonResult refreshThings() throws Exception {
        JwtToken token = (JwtToken) SecurityUtils.getSubject().getPrincipal();
        Integer id = Integer.valueOf(JwtUtils.getClaimByToken(token.token).getSubject());
        QueryWrapper<Thingstable> queryWrapper = new QueryWrapper<Thingstable>()
                .eq("userid",id);
        List<Thingstable> tabledata = thingstableMapper.selectList(queryWrapper);
        LocalDateTime time = LocalDateTime.now();
        if(tabledata==null){
            return new JsonResult().error("Don't have items");
        }
        quartzservice.startThings(tabledata
                .parallelStream()
                .filter(item-> thingService.checkAndSetStatus(item).equals("Running"))
                .collect(Collectors.toList()));
        return new JsonResult().ok(tabledata);
    }

    @PostMapping("/changeitem")
    @RequiresAuthentication
    public JsonResult changeitem(@RequestBody ItemVOEntity item) throws Exception {
        QueryWrapper<Thingstable> queryWrapper = new QueryWrapper<Thingstable>()
                .eq("id",item.getId());
        Thingstable thing = iThingstableService.getOne(queryWrapper);
        if(thing==null){
            return new JsonResult().error("Don't Exist");
        }else {
            List<Thingstable> listpre = new ArrayList<Thingstable>();
            listpre.add(thing);
            quartzservice.delthings(listpre);

            thing.setName(item.getName());
            thing.setStartTime(item.getStartTime());
            thing.setEndTime(item.getEndTime());
            thing.setMessage(item.getMessage());
            thing.setTag(item.getTag());
            thing.setType(item.getType());
            thing.setAlertToken(item.getAlertToken());

            if(thingstableMapper.updateById(thing)>0){
                if(thing.getStatus().equals("Running")){
                    List<Thingstable> listnow = new ArrayList<Thingstable>();
                    listnow.add(thing);
                    quartzservice.startThings(listnow);
                }
                return new JsonResult().ok("insert successfully");
            }
            else {
                return new JsonResult().error("insert faild");
            }
        }
    }
    @GetMapping("/delitem")
    @RequiresAuthentication
    public JsonResult delitem(Integer id) {
        QueryWrapper<Thingstable> queryWrapper = new QueryWrapper<Thingstable>()
                .eq("id",id);
        Thingstable thing = iThingstableService.getOne(queryWrapper);
        if (thing == null) {
            return new JsonResult().error("Don't Exist");
        } else {
            if (thingstableMapper.deleteById(id) > 0) {
                return new JsonResult().ok("del successfully");
            } else {
                return new JsonResult().error("del faild");
            }
        }
    }
    @GetMapping("/startitem")
    @RequiresAuthentication
    public JsonResult startItem(Integer id) throws Exception {
        QueryWrapper<Thingstable> queryWrapper = new QueryWrapper<Thingstable>()
                .eq("id",id);
        Thingstable thing = iThingstableService.getOne(queryWrapper);
        if (thing == null) {
            return new JsonResult().error("Don't Exist");
        }else {
            LocalDateTime now = LocalDateTime.now();
            if(thing.getStatus()=="Running"){
                return new JsonResult().error("Already Running");
            }else if(thing.getEndTime().isBefore(now)){
                thing.setStatus("Expired");
                iThingstableService.updateById(thing);
                return new JsonResult().error("Expired");
            }else {
                thing.setStatus("Running");
                List<Thingstable> list = new ArrayList<Thingstable>();
                list.add(thing);
                if(quartzservice.startThings(list)){
                    thingstableMapper.updateById(thing);
                    return new JsonResult().ok("started");
                }else {
                    return new JsonResult().error("faild to start");
                }
            }
        }
    }
    @GetMapping("/pauseitem")
    @RequiresAuthentication
    public JsonResult pauseitem(Integer id) throws Exception {
        QueryWrapper<Thingstable> queryWrapper = new QueryWrapper<Thingstable>()
                .eq("id",id);
        List<Thingstable> list  = iThingstableService.list(queryWrapper);
        if(list!=null){
            list.forEach(item->item.setStatus("Pause"));
            quartzservice.pausethings(list);
            if(iThingstableService.updateBatchById(list)){
                return new JsonResult().ok("Stopped");
            }
        }else {
            return new JsonResult().ok("Item not found");
        }
        return new JsonResult().ok("Stop Faild");
    }
    @GetMapping("/initstart")
    public JsonResult initstart() throws Exception {
        JwtToken token = (JwtToken) SecurityUtils.getSubject().getPrincipal();
        Integer id = Integer.valueOf(JwtUtils.getClaimByToken(token.token).getSubject());
        QueryWrapper<Thingstable> queryWrapper = new QueryWrapper<Thingstable>()
                .eq("id",id);
        quartzservice.initstart();
        quartzservice.startThings(iThingstableService.list(queryWrapper));
        return new JsonResult().ok("init successfully");
    }

}
