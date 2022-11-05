package com.vueespring.service.serviceImpl;

import com.vueespring.Scheduler.FWAlertJob;
import com.vueespring.entity.Thingstable;
import com.vueespring.service.ThingService;
import com.vueespring.utils.SchedulerUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
public class ThingServiceImpl implements ThingService {
    @Autowired
    SchedulerUtils schedulerUtils;

    @Override
    public void creatitem(Thingstable thing, Scheduler scheduler) throws SchedulerException {
        Integer type = thing.getType();
        JobDataMap map = new JobDataMap();
        map.put("message",thing.getMessage());
        map.put("start_time",thing.getStartTime());
        map.put("end_time",thing.getEndTime());
        map.put("type",thing.getType());
        map.put("tag",thing.getTag());
        map.put("name",thing.getName());
        map.put("alertToken",thing.getAlertToken());
        String token = thing.getAlertToken();
        if(thing.getAlertToken()!=null){
            map.put("alertToken",token);
        }
        JobDetail job = JobBuilder.newJob(FWAlertJob.class)
                .withIdentity(thing.getName(),thing.getTag())
                .usingJobData(map)
                .build();
        Instant start = thing.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
        Instant end = thing.getEndTime().atZone(ZoneId.systemDefault()).toInstant();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(thing.getName(),thing.getTag())
                .startAt(Date.from(start))
                .endAt(Date.from(end))
                .withSchedule(schedulerUtils.getInterval(type).repeatForever())
                .usingJobData(map)
                .build();
        scheduler.scheduleJob(job,trigger);
    }
    @Override
    public void startitem(Thingstable thingstable, Scheduler scheduler) throws SchedulerException {
        JobKey key = JobKey.jobKey(thingstable.getName(),thingstable.getTag());
        if(scheduler.checkExists(key)){
            scheduler.resumeJob(key);
            System.out.println("Starting Job "+key);
        }else {
            this.creatitem(thingstable,scheduler);
        }
    }
    @Override
    public void pausething(Thingstable thing, Scheduler scheduler){
        JobKey key = JobKey.jobKey(thing.getName(),thing.getTag());
        try {
            scheduler.pauseJob(key);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delthing(Thingstable thing,Scheduler scheduler) throws SchedulerException {
        JobKey key = JobKey.jobKey(thing.getName(),thing.getTag());
        scheduler.deleteJob(key);
    }
    @Override
    public String checkAndSetStatus(Thingstable thing){
        if(thing.getEndTime().isBefore(LocalDateTime.now())){
            thing.setStatus("Expired");
            return "Expired";
        }else {
            return thing.getStatus();
        }
    }
}
