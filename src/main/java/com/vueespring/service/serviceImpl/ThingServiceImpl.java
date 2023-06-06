package com.vueespring.service.serviceImpl;

import com.vueespring.Scheduler.FWPushingJob;
import com.vueespring.entity.ThingEnity;
import com.vueespring.entity.WebEntity.Item.Itemtity;
import com.vueespring.entity.WebEntity.UserEntity;
import com.vueespring.service.QuartzService;
import com.vueespring.service.ThingService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
@Slf4j
public class ThingServiceImpl implements ThingService {
    @Autowired
    QuartzService quartzService;

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
                .withSchedule(quartzService.getInterval(type).repeatForever())
                .usingJobData(map)
                .build();
        scheduler.scheduleJob(job, trigger);
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
    public void pausething(ThingEnity thing, Scheduler scheduler) {
        JobKey key = JobKey.jobKey(thing.getName(), thing.getTag());
        try {
            scheduler.pauseJob(key);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delthing(ThingEnity thing, Scheduler scheduler) throws SchedulerException {
        JobKey key = JobKey.jobKey(thing.getName(), thing.getTag());
        scheduler.deleteJob(key);
    }

    @Override
    public String checkAndSetStatus(ThingEnity thing) {
        if (thing.getEndTime().isBefore(LocalDateTime.now())) {
            thing.setStatus("Expired");
            return "Expired";
        } else {
            return thing.getStatus();
        }
    }

    @Override
    public ThingEnity getThingByVoe(Itemtity itemtity, String userid, UserEntity userinfo) {
        ThingEnity thingEnity = new ThingEnity();
        thingEnity.setName(itemtity.getName());
        thingEnity.setStartTime(itemtity.getStartTime());
        thingEnity.setEndTime(itemtity.getEndTime());
        thingEnity.setMessage(itemtity.getMessage());
        thingEnity.setType(itemtity.getType());
        thingEnity.setTag(itemtity.getTag());
        thingEnity.setUserid(userid);
        thingEnity.setCreater(userinfo.getUsername());
        thingEnity.setAlertToken(userinfo.getAlertToken());
        thingEnity.setStatus("Pause");
        return thingEnity;
    }
}
