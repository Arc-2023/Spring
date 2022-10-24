package com.vueespring.Scheduler;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
public class TestJob extends QuartzJobBean {
    //@Autowired
    //IThingstableService iThingstableService;
    /**
     * @param jobExecutionContext
     */
    @Override
    protected void executeInternal(org.quartz.JobExecutionContext jobExecutionContext) {
        JobDetail detail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = detail.getJobDataMap();
        if(jobDataMap.get("token")!=null){
            JSONObject data = null;
            try {
                data = new JSONObject()
                        .put("message",jobDataMap.get("message"))
                        .put("start",jobDataMap.get("start_time"))
                        .put("end",jobDataMap.get("end_time"))
                        .put("type",jobDataMap.get("type"))
                        .put("tag",jobDataMap.get("tag"))
                        .put("name",jobDataMap.get("name"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            HttpResponse res = HttpUtil.createGet("https://fwalert.com/"+jobDataMap.get("token"))
                    .body(data.toString())
                    .execute();

        }
    }
}
