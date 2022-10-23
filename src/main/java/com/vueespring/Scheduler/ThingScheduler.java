package com.vueespring.Scheduler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueespring.entity.Thingstable;
import com.vueespring.mapper.ThingstableMapper;
import com.vueespring.service.IThingstableService;
import com.vueespring.utils.SchedulerUtils;
import lombok.Data;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
@Data
@Component
public class ThingScheduler {
    @Autowired
    public IThingstableService iThingstableService;
    @Autowired
    public  ThingstableMapper mapper;
    @Autowired
    public  SchedulerUtils schedulerUtils;
    public static void main(String[] args) throws Exception {
    }
    public void init(String username) throws Exception {
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        QueryWrapper<Thingstable> queryWrapper =new QueryWrapper<Thingstable>();
        List<Thingstable> things = mapper.selectList(queryWrapper);
        defaultScheduler.clear();
        things.forEach(thing->{
            Integer type = thing.getType();
            JobDataMap map =new JobDataMap();
            map.put("username",thing.getName());
            map.put("message",thing.getMessage());
            JobDetail job = JobBuilder.newJob(TestJob.class)
                    .withIdentity(thing.getName(),thing.getName())
                    .usingJobData(map)
                    .build();

            Instant start = thing.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
            Instant end = thing.getEndTime().atZone(ZoneId.systemDefault()).toInstant();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(thing.getName(),thing.getName())
                    .startAt(Date.from(start))
                    .endAt(Date.from(end))
                    .withSchedule(schedulerUtils.getInterval(type))
                    .usingJobData(map)
                    .build();
            try {

                defaultScheduler.scheduleJob(job,trigger);

            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
        defaultScheduler.start();
    }
}
