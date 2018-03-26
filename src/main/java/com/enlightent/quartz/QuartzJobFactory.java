package com.enlightent.quartz;
 
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
 
import com.enlightent.entity.ScheduleTask;
import com.enlightent.service.ScheduleConfigService;
import com.enlightent.util.DingdingUtils;
import com.enlightent.util.SpringUtils;
 
public class QuartzJobFactory implements Job{
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleTask wt = (ScheduleTask)context.getMergedJobDataMap().get("scheduleTask");
        try {
            String config = wt.getConfig();
            if (StringUtils.isNotBlank(config)) {
                ScheduleConfigService s = SpringUtils.getBean("scheduleConfigService");
                s.execute(wt);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            String message = e.getMessage();
            if (message != null) {
                if (message.length() > 1000) {
                    message = message.substring(0, 999);
                }
            }
            StringBuilder sBuilder = new StringBuilder(300);
            sBuilder.append(wt.getId()).append("：").append(wt.getTitle()).append(DingdingUtils.NEW_LINE);
            sBuilder.append("任务执行失败！").append(DingdingUtils.NEW_LINE);
            sBuilder.append("Exception：").append(message);
            DingdingUtils.sendToDing(wt.getTitle(), sBuilder.toString());
        }
//        ScheduleTaskUtil.invokMethod(wt);
    }
 
}