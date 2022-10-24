package com.vueespring.utils;

import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Component;

@Component
public class SchedulerUtils {
    public SimpleScheduleBuilder getInterval(Integer type){
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .repeatForever();
        if(type==1){
            simpleScheduleBuilder.withIntervalInMinutes(1);
        }else if(type==2){
            simpleScheduleBuilder.withIntervalInMinutes(10);
        }else if(type==3){
            simpleScheduleBuilder.withIntervalInMinutes(30);
        }else if(type==4){
            simpleScheduleBuilder.withIntervalInHours(2);
        }else if(type==5){
            simpleScheduleBuilder.withIntervalInHours(6);
        }else if(type==6){
            simpleScheduleBuilder.withIntervalInHours(24);
        }
        return simpleScheduleBuilder;
    }
}
