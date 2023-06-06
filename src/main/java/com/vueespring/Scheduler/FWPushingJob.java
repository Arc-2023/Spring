package com.vueespring.Scheduler;
import com.vueespring.service.FWAlertJobService;
import com.vueespring.service.serviceImpl.FWAlertJobServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class FWPushingJob extends QuartzJobBean {
    FWAlertJobService fwAlertJobService = new FWAlertJobServiceImpl();
    @Override
    protected void executeInternal(org.quartz.JobExecutionContext jobExecutionContext) {
        JobDetail detail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = detail.getJobDataMap();
        Map<String,Object> map = new HashMap<>();
        log.debug(String.valueOf(jobDataMap));
        fwAlertJobService.setMapByJob(jobDataMap, map);

        if(LocalDateTime.now().isAfter((LocalDateTime)map.get("end"))){
            String message = "Last Mention: " + map.get("message");
            map.put("message",message);
        }
        if(map.get("alertToken")==null){
            log.error("token empty!");
        }
        else{fwAlertJobService.sendFWAlert(map);}
    }
}
