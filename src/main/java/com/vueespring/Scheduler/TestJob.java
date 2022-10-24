package com.vueespring.Scheduler;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
public class TestJob extends QuartzJobBean {
    //@Autowired
    //IThingstableService iThingstableService;
    /**
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(org.quartz.JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail detail = jobExecutionContext.getJobDetail();
        String obj = detail.getJobDataMap().get("message").toString();
    }
}
