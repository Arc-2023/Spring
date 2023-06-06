package com.vueespring.service;

import io.swagger.annotations.SecurityDefinition;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;

import java.util.Map;
public interface FWAlertJobService {
    void sendFWAlert(Map<String, Object> map);

    void setMapByJob(JobDataMap jobDataMap, Map<String, Object> map);
}
