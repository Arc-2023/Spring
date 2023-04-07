package com.vueespring.service.serviceImpl;

import com.vueespring.entity.ThingEnity;
import com.vueespring.service.ThingsService;
import com.vueespring.service.QuartzService;
import com.vueespring.service.ThingService;
import lombok.Data;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class QuartzServiceImpl implements QuartzService {
    @Autowired
    public ThingsService iThingstableService;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    public ThingService thingService;
    @Override
    public Boolean initstart() throws SchedulerException {
        scheduler.clear();
        scheduler.start();
        return true;
    }
    @Override
    public Boolean startThings(List<ThingEnity> list) throws Exception{
        list.parallelStream().forEach(thing->{
            try {
                thingService.startitem(thing,scheduler);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }
    @Override
    public Boolean pausethings(List<ThingEnity> list) throws Exception{
        list.stream().parallel().forEach(thing->{
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
    @Override
    public SimpleScheduleBuilder getInterval(Integer type){
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .repeatForever();
        simpleScheduleBuilder.withIntervalInHours(type);
        return simpleScheduleBuilder;
    }

}
