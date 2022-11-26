package com.vueespring.Scheduler;
import com.vueespring.service.FWAlertJobService;
import com.vueespring.service.serviceImpl.FWAlertJobServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class FWPushingJob extends QuartzJobBean {
    FWAlertJobService fwAlertJobService = new FWAlertJobServiceImpl();
    /**
     * @param jobExecutionContext
     */
    @Override
    protected void executeInternal(org.quartz.JobExecutionContext jobExecutionContext) {
        JobDetail detail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = detail.getJobDataMap();
        Map<String,Object> map = new HashMap<>();
        log.debug(String.valueOf(jobDataMap));
        String token = "79bd4013-bd17-4c3e-9371-9119849790f7";
        fwAlertJobService.setMapByJob(jobDataMap, map);
        fwAlertJobService.sendFWAlert(map, token);
    }
}
