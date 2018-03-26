package com.enlightent.quartz;
 
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.UUID;
 
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
 
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.enlightent.been.Paging;
import com.enlightent.entity.JobAndTrigger;
import com.enlightent.entity.ScheduleTask;
import com.enlightent.repository.ScheduleTaskRepository;
import com.enlightent.util.HibernateUtils;
 
@Service
public class ScheduleTaskJobService {
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
 
    @Autowired
    private Scheduler scheduler;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PostConstruct
    public void init() throws Exception {
//        List<ScheduleTask> findByStatus = scheduleTaskRepository.findByStatus(1);
//        for (ScheduleTask scheduleTask : findByStatus) {
//            addJob(scheduleTask);
//        }
    }
    
    public void addJob(ScheduleTask job) throws SchedulerException {
        if (job == null || !ScheduleTask.STATUS_RUNNING.equals(job.getStatus())) {
            return;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getId() + "");
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        
        // 不存在，创建一个
        if (null == trigger) {
            Class clazz = QuartzJobFactory.class;
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getId() + "").build();
            jobDetail.getJobDataMap().put("scheduleTask", job);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getId() + "").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
 
            JobKey jobKey = new JobKey(job.getId() + "");
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            //更新job的参数 比如cronExpression的更新
            jobDetail.getJobDataMap().put("scheduleTask", job);
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }
    
    
    /**
     * 暂停一个job
     * 
     */
    public void pauseJob(ScheduleTask wt) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(wt.getId() + "");
        scheduler.pauseJob(jobKey);
    }
 
    /**
     * 恢复一个job
     * 
     */
    public void resumeJob(ScheduleTask wt) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(wt.getId() + "");
        scheduler.resumeJob(jobKey);
    }
 
    /**
     * 删除一个job
     * 
     * @throws SchedulerException
     */
    public void deleteJob(ScheduleTask wt) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(wt.getId() + "");
        scheduler.deleteJob(jobKey);
    }
 
    /**
     * 立即执行job
     * 
     * @throws SchedulerException
     */
    public void runAJobNow(ScheduleTask job) throws SchedulerException {
        Class clazz = QuartzJobFactory.class;
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(UUID.randomUUID().toString()).build();
        jobDetail.getJobDataMap().put("scheduleTask", job);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString()).startNow().build();
        scheduler.scheduleJob(jobDetail, trigger);
 
    }
 
    /**
     * 更新job时间表达式
     * 
     * @throws SchedulerException
     */
    public void updateJobCron(ScheduleTask wt) throws SchedulerException {
 
        TriggerKey triggerKey = TriggerKey.triggerKey(wt.getId() + "");
 
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
 
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(wt.getCronExpression());
 
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
 
        scheduler.rescheduleJob(triggerKey, trigger);
    }
    
    public Paging<JobAndTrigger> getJobAndTriggerDetails(String jobName, int pageNum, int pageSize, String sort, String order) throws Exception {
        int fromIndex = (pageNum -1)* pageSize;
        
        StringBuilder sqlString = new StringBuilder(200);
        sqlString.append("SELECT QRTZ_JOB_DETAILS.JOB_NAME, QRTZ_JOB_DETAILS.JOB_GROUP, QRTZ_JOB_DETAILS.JOB_CLASS_NAME,QRTZ_JOB_DETAILS.JOB_DATA, "
                + "QRTZ_TRIGGERS.TRIGGER_NAME, QRTZ_TRIGGERS.TRIGGER_GROUP,QRTZ_TRIGGERS.PREV_FIRE_TIME,QRTZ_TRIGGERS.START_TIME,QRTZ_TRIGGERS.NEXT_FIRE_TIME,QRTZ_TRIGGERS.TRIGGER_STATE,QRTZ_CRON_TRIGGERS.CRON_EXPRESSION,QRTZ_CRON_TRIGGERS.TIME_ZONE_ID FROM "
                + "schedule.QRTZ_JOB_DETAILS JOIN schedule.QRTZ_TRIGGERS JOIN schedule.QRTZ_CRON_TRIGGERS ON QRTZ_JOB_DETAILS.JOB_NAME = QRTZ_TRIGGERS.JOB_NAME "
                + "AND QRTZ_TRIGGERS.TRIGGER_NAME = QRTZ_CRON_TRIGGERS.TRIGGER_NAME "
                + "AND QRTZ_TRIGGERS.TRIGGER_GROUP = QRTZ_CRON_TRIGGERS.TRIGGER_GROUP ");
        if(StringUtils.isNotBlank(jobName)){
            sqlString.append(" and QRTZ_JOB_DETAILS.JOB_NAME = :jobName ");
        }
        sqlString.append(" order by " + sort + " " + order);
        sqlString.append(" limit :fromIndex , :size ");
        Query query = entityManager.createNativeQuery(sqlString.toString());
        if(StringUtils.isNotBlank(jobName)){
            query.setParameter("jobName", jobName);
        }
        query.setParameter("fromIndex", fromIndex);
        query.setParameter("size", pageSize);
 
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List result = query.getResultList();
        List<JobAndTrigger> entityList = HibernateUtils.convertList(JobAndTrigger.class, result);
        if(CollectionUtils.isNotEmpty(entityList)){
        	ObjectInputStream in = null;
        	for (JobAndTrigger jobAndTrigger : entityList) {
        		in = new ObjectInputStream(new ByteArrayInputStream(jobAndTrigger.getJOB_DATA()));
        		JobDataMap obj = (JobDataMap)in.readObject();
        		ScheduleTask scheduleTask = (ScheduleTask) obj.get("scheduleTask");
        		jobAndTrigger.setJOB_DATA(null);
        		jobAndTrigger.setScheduleTask(scheduleTask);
        	}
        }
        
        Paging<JobAndTrigger> page = new Paging<JobAndTrigger>();
        page.setRows(entityList);
        page.setTotal(getTotal(jobName));
        return page;
    }
    
    public Long getTotal(String jobName) {
        StringBuilder sqlString = new StringBuilder(200);
        sqlString.append("SELECT count(*) FROM "
                + "schedule.QRTZ_JOB_DETAILS JOIN schedule.QRTZ_TRIGGERS JOIN schedule.QRTZ_CRON_TRIGGERS ON QRTZ_JOB_DETAILS.JOB_NAME = QRTZ_TRIGGERS.JOB_NAME "
                + "AND QRTZ_TRIGGERS.TRIGGER_NAME = QRTZ_CRON_TRIGGERS.TRIGGER_NAME "
                + "AND QRTZ_TRIGGERS.TRIGGER_GROUP = QRTZ_CRON_TRIGGERS.TRIGGER_GROUP");
        if(StringUtils.isNotBlank(jobName)){
            sqlString.append(" and QRTZ_JOB_DETAILS.JOB_NAME = :jobName ");
        }
        Query query = entityManager.createNativeQuery(sqlString.toString());
        if(StringUtils.isNotBlank(jobName)){
            query.setParameter("jobName", jobName);
        }
        List resultList = query.getResultList();
        if(CollectionUtils.isNotEmpty(resultList)){
            return Long.parseLong(resultList.get(0).toString());
        }
        return 0l;
    }
    
}
