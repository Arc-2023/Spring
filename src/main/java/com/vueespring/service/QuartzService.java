package com.vueespring.service;

import com.vueespring.entity.ThingEnity;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;

import java.util.List;

public interface QuartzService {
    Boolean initstart() throws SchedulerException;

    Boolean startThings(List<ThingEnity> list) throws Exception;

    Boolean pausethings(List<ThingEnity> list) throws Exception;

    Boolean delthings(List<ThingEnity> list) throws Exception;

    SimpleScheduleBuilder getInterval(Integer type);

    void pausething(ThingEnity thing, Scheduler scheduler);

    void startitem(ThingEnity thingEnity, Scheduler scheduler) throws SchedulerException;

    void delthing(ThingEnity thing, Scheduler scheduler) throws SchedulerException;

    void creatitem(ThingEnity thing, Scheduler scheduler) throws SchedulerException;
}