package com.vueespring.service;

import com.vueespring.entity.ThingEnity;
import com.vueespring.entity.WebEntity.Item.Itemtity;
import com.vueespring.entity.WebEntity.UserEntity;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;


public interface ThingService {
    void creatitem(ThingEnity thing, Scheduler scheduler) throws SchedulerException;

    void startitem(ThingEnity thingEnity, Scheduler scheduler) throws SchedulerException;

    void pausething(ThingEnity thing, Scheduler scheduler);

    void delthing(ThingEnity thing, Scheduler scheduler) throws SchedulerException;

    String checkAndSetStatus(ThingEnity thing);

    ThingEnity getThingByVoe(Itemtity itemtity, String userid, UserEntity userinfo);

    SimpleScheduleBuilder getInterval(Integer type);
}
