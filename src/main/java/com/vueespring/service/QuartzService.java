package com.vueespring.service;

import com.vueespring.entity.Thingstable;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

public interface QuartzService {
    Boolean initstart() throws SchedulerException;

    Boolean startThings(List<Thingstable> list) throws Exception;

    Boolean pausethings(List<Thingstable> list) throws Exception;

    Boolean delthings(List<Thingstable> list) throws Exception;

    SimpleScheduleBuilder getInterval(Integer type);
}
