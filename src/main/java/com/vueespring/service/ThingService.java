package com.vueespring.service;

import com.vueespring.entity.Thingstable;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


public interface ThingService {
    void creatitem(Thingstable thing, Scheduler scheduler) throws SchedulerException;

    void startitem(Thingstable thingstable, Scheduler scheduler) throws SchedulerException;

    void pausething(Thingstable thing, Scheduler scheduler);

    void delthing(Thingstable thing, Scheduler scheduler) throws SchedulerException;

    String checkAndSetStatus(Thingstable thing, LocalDateTime time);
}
