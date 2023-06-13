package com.vueespring.service.serviceImpl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.vueespring.service.FWAlertJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
@Service
public class FWAlertJobServiceImpl implements FWAlertJobService {
    @Override
    public void sendFWAlert(Map<String, Object> map) {
        log.debug(String.valueOf(map));
        HttpResponse res = HttpUtil.createGet("https://fwalert.com/"+ map.get("alertToken"))
                        .form(map)
                        .execute();
        log.debug(String.valueOf(res));
    }
    @Override
    public void setMapByJob(JobDataMap jobDataMap, Map<String, Object> map) {
        map.put("message", jobDataMap.get("message"));
        map.put("start", jobDataMap.get("start_time"));
        map.put("end", jobDataMap.get("end_time"));
        map.put("type", jobDataMap.get("type"));
        map.put("tag", jobDataMap.get("tag"));
        map.put("name", jobDataMap.get("name"));
        map.put("alertToken",jobDataMap.get("alertToken"));
    }
}
