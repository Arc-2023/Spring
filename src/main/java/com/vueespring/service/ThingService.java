package com.vueespring.service;

import com.vueespring.entity.Thingstable;
import com.vueespring.entity.WebEntity.Item.ItemVOEntity;
import com.vueespring.entity.WebEntity.UserVoeEntity;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;


public interface ThingService {
    void creatitem(Thingstable thing, Scheduler scheduler) throws SchedulerException;

    void startitem(Thingstable thingstable, Scheduler scheduler) throws SchedulerException;

    void pausething(Thingstable thing, Scheduler scheduler);

    void delthing(Thingstable thing, Scheduler scheduler) throws SchedulerException;

    String checkAndSetStatus(Thingstable thing);

    Thingstable getThingByVoe(ItemVOEntity itemVOEntity, String userid, UserVoeEntity userinfo);
}
