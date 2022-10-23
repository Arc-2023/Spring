package com.vueespring.Scheduler;
import com.vueespring.service.IThingstableService;
import com.vueespring.utils.WXUtils;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
public class TestJob extends QuartzJobBean {
    @Autowired
    IThingstableService iThingstableService;
    /**
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(org.quartz.JobExecutionContext jobExecutionContext) throws JobExecutionException {
        WXUtils wxUtils1 = new WXUtils();
        JobDetail detail = jobExecutionContext.getJobDetail();
        String obj = detail.getJobDataMap().get("message").toString();
        if(!wxUtils1.sendMsg((String) obj)){
            System.out.println("Send Faild");
        }
    }
}
