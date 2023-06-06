package com.vueespring.service.serviceImpl;

import com.vueespring.Scheduler.FWPushingJob;
import com.vueespring.entity.ThingEnity;
import com.vueespring.service.ThingsService;
import com.vueespring.service.QuartzService;
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
    public ThingsService iThingstableService;
    @Autowired
    private Scheduler scheduler;

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
                startitem(thing,scheduler);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }
    @Override
    public Boolean pausethings(List<ThingEnity> list) throws Exception{
        list.forEach(thing->{
            pausething(thing, scheduler);
        });
        return true;
    }
    @Override
    public Boolean delthings(List<ThingEnity> list) throws Exception{
        list.forEach(thing->{
            try {
                delthing(thing,scheduler);
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
    @Override
    public void pausething(ThingEnity thing, Scheduler scheduler) {
        JobKey key = JobKey.jobKey(thing.getName(), thing.getTag());
        try {
            scheduler.pauseJob(key);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void startitem(ThingEnity thingEnity, Scheduler scheduler) throws SchedulerException {
        JobKey key = JobKey.jobKey(thingEnity.getName(), thingEnity.getTag());
        if (scheduler.checkExists(key)) {
            scheduler.resumeJob(key);
            System.out.println("Resuming Job " + key);
        } else {
            this.creatitem(thingEnity, scheduler);
        }
    }
    @Override
    public void delthing(ThingEnity thing, Scheduler scheduler) throws SchedulerException {
        JobKey key = JobKey.jobKey(thing.getName(), thing.getTag());
        scheduler.deleteJob(key);
    }
    @Override
    public void creatitem(ThingEnity thing, Scheduler scheduler) throws SchedulerException {
        Integer type = thing.getType();
        JobDataMap map = new JobDataMap();
        map.put("message", thing.getMessage());
        map.put("start_time", thing.getStartTime());
        map.put("end_time", thing.getEndTime());
        map.put("type", thing.getType());
        map.put("tag", thing.getTag());
        map.put("name", thing.getName());
        map.put("userId", thing.getUserid());
        if (thing.getAlertToken() == null) {
            log.error("empty token: " + thing);
            return;
        } else {
            map.put("alertToken", thing.getAlertToken());
        }
        JobDetail job = JobBuilder.newJob(FWPushingJob.class)
                .withIdentity(thing.getName(), thing.getTag())
                .usingJobData(map)
                .build();
        Instant start = thing.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
        Instant end = thing.getEndTime().atZone(ZoneId.systemDefault()).toInstant();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(thing.getName(), thing.getTag())
                .startAt(Date.from(start))
                .endAt(Date.from(end))
                .withSchedule(getInterval(type).repeatForever())
                .usingJobData(map)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

}
