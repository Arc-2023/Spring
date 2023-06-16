package com.vueespring.service.serviceImpl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.vueespring.Scheduler.FWPushingJob;
import com.vueespring.entity.ThingEnity;
import com.vueespring.service.QuartzService;
import com.vueespring.service.ThingService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@Slf4j
public class QuartzServiceImpl implements QuartzService {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ThingService thingService;

    @Override
    public Boolean initstart() throws SchedulerException {
        scheduler.clear();
        scheduler.start();
        return true;
    }
    @Override
    public Boolean startThings(List<ThingEnity> list) {
        list.forEach(thing->{
            try {
                thingService.startitem(thing,scheduler);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }
    @Override
    public Boolean pausethings(List<ThingEnity> list) {
        list.forEach(thing->{
            thingService.pausething(thing, scheduler);
        });
        return true;
    }
    @Override
    public Boolean delthings(List<ThingEnity> list) throws Exception{
        list.forEach(thing->{
            try {
                thingService.delthing(thing,scheduler);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

}
