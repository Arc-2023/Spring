package com.vueespring.service.serviceImpl;

import com.vueespring.entity.Thingstable;
import com.vueespring.mapper.ThingstableMapper;
import com.vueespring.service.IThingstableService;
import com.vueespring.service.QuartzService;
import com.vueespring.service.ThingService;
import lombok.Data;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Component
@Service
public class QuartzServiceImpl implements QuartzService {
    @Autowired
    public IThingstableService iThingstableService;
    @Autowired
    public ThingstableMapper mapper;
    private Scheduler scheduler;
    @Autowired
    public ThingService thingService;
    @Override
    public Boolean initstart() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.clear();
        scheduler.start();
        return true;
    }
    @Override
    public Boolean startThings(List<Thingstable> list) throws Exception{
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
    public Boolean pausethings(List<Thingstable> list) throws Exception{
        list.stream().forEach(thing->{
            thingService.pausething(thing,scheduler);
        });
        return true;
    }
    @Override
    public Boolean delthings(List<Thingstable> list) throws Exception{
        list.stream().forEach(thing->{
            try {
                thingService.delthing(thing,scheduler);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

}
