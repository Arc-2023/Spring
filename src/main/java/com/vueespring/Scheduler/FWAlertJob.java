package com.vueespring.Scheduler;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.Map;

public class FWAlertJob extends QuartzJobBean {
    /**
     * @param jobExecutionContext
     */
    @Override
    protected void executeInternal(org.quartz.JobExecutionContext jobExecutionContext) {
        JobDetail detail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = detail.getJobDataMap();
        Map<String,Object> map = new HashMap<String,Object>();
        System.out.println(jobDataMap);
        String token = "79bd4013-bd17-4c3e-9371-9119849790f7";
        map.put("message",jobDataMap.get("message"));
        map.put("start",jobDataMap.get("start_time"));
        map.put("end",jobDataMap.get("end_time"));
        map.put("type",jobDataMap.get("type"));
        map.put("tag",jobDataMap.get("tag"));
        map.put("name",jobDataMap.get("name"));
        System.out.println(map);
        HttpResponse res = HttpUtil.createGet("https://fwalert.com/"+token)
                        .form(map)
                        .execute();
        System.out.println(res);
    }
}
