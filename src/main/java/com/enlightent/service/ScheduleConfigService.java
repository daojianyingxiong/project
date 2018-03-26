package com.enlightent.service;
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enlightent.been.PrintResult;
import com.enlightent.entity.ScheduleTask;
import com.enlightent.repository.ScheduleTaskRepository;
import com.enlightent.util.CalendarUtil;
import com.enlightent.util.DingdingUtils;
import com.enlightent.util.ExpressionUtils;
 
@Service
public class ScheduleConfigService {
 
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Autowired
    private ScheduleTaskInfoService scheduleTaskInfoService;
 
    @Autowired
    private ResultHandleService resultHandleService;
    
    public String buildCheckSql(Map param, String outputSQL) {
        int i = 0;
        Collection values = param.values();
        for (Object obj : values) {
            if (obj == null) {
                obj = "null";
            }
            outputSQL = outputSQL.replace("?" + i, obj.toString());
            i++;
        }
        return outputSQL;
    }
 
    private Map<String, Object> getConfigMap(String xml) {
        JSONObject jsonObject = XML.toJSONObject(xml);
        Map<String, Object> map = jsonObject.toMap();
        return map;
    }
 
    public void execute(ScheduleTask scheduleTask) {
        StringBuilder exLog = null;
        Integer printCount = null;
        try {
            scheduleTask.setLastFinishedDate(CalendarUtil.changeDateToStr(new Date()));
            Map<String, Object> configMap = getConfigMap(scheduleTask.getConfig());
            Map<String, String> paramSqlMap = (Map<String, String>) configMap.get("paramSql");
            Map<String, Object> checkSqlMap = (Map<String, Object>) configMap.get("checkSql");
            
            Map<String, Object> print = (Map<String, Object>) checkSqlMap.get("print");
            PrintResult printResult = getPrintResult(print);
            Boolean debug = (Boolean) configMap.get("debug");
            if (debug != null && debug) {
                exLog = new StringBuilder(300);
            }
            
            if (paramSqlMap != null) {
                paramSqlMap.get("dataSource");
                String paramSql = paramSqlMap.get("sql");
                List resultList = (List) resultHandleService.executeSql(paramSql, paramSqlMap.get("dataSource"), exLog);
                String sql = (String) checkSqlMap.get("sql");
                this.unionDispose(resultList, scheduleTask, sql, checkSqlMap, exLog, printResult);
            } else {
                printResult.setPrintValue(true);
                printResult.setPrintParam(false);
                printResult.setPrintSql(false);
                this.simpleDispose(scheduleTask, checkSqlMap, exLog, printResult);
            }
            resultHandleService.sendMessage(scheduleTask, printResult);
            printCount = printResult.getTotal();
        } catch (Throwable e) {
            e.printStackTrace();
            String message = e.getMessage();
            if (message != null) {
                if (message.length() > 1000) {
                    message = message.substring(0, 999);
                }
            }
            String title = scheduleTask.getTitle();
            StringBuilder sBuilder = new StringBuilder(300);
            sBuilder.append(scheduleTask.getId()).append("：").append(title).append(DingdingUtils.NEW_LINE);
            sBuilder.append("任务执行失败！").append(DingdingUtils.NEW_LINE);
            sBuilder.append("Exception：").append(message);
            String string = sBuilder.toString();
            DingdingUtils.sendToDing(title, string);
            scheduleTask.setMessage(message);
            scheduleTask.setFailed(true);
        }
        
        scheduleTaskInfoService.save(scheduleTask, exLog, printCount);
        
        Integer findTotal = scheduleTask.getFindTotal();
        Integer errTotal = scheduleTask.getErrTotal();
        scheduleTaskRepository.updateLastDate(scheduleTask.getId(), scheduleTask.getLastFinishedDate(), findTotal, errTotal, scheduleTask.getMessage());
 
    }
 
    private PrintResult getPrintResult(Map<String, Object> print) {
        PrintResult printResult = new PrintResult();
        if (print != null) {
            String exceptParams = (String) print.get("exceptParams");
            if (StringUtils.isNotBlank(exceptParams)) {
                Set<String> excepts = printResult.getExcepts();
                String[] split = exceptParams.split(",");
                for (String string : split) {
                    excepts.add(string.trim());
                }
            }
            
            String statement = (String) print.get("statement");
            if (StringUtils.isNotBlank(statement)) {
                String[] split = statement.split(",");
                for (String string : split) {
                    if (string.trim().equals("param")) {
                        printResult.setPrintParam(true);
                    } else if (string.trim().equals("value")) {
                        printResult.setPrintValue(true);
                    }
                }
            } else {
                printResult.setPrintParam(true);
            }
            
            Object sql = print.get("sql");
            if (sql != null) {
                if (sql.toString().equals("0")) {
                    printResult.setPrintSql(false);
                }
            }
            
            Object url = print.get("url");
            if (url != null) {
                if (url.toString().equals("0")) {
                    printResult.setPrintDetailUrl(false);
                }
            }
            
            Object previous = print.get("previous");
            if (previous != null) {
            	printResult.setPrevious(Integer.parseInt(previous.toString()));
			}
        } else {
            printResult.setPrintParam(true);
        }
        return printResult;
    }
 
    private StringBuilder simpleDispose(ScheduleTask scheduleTask, Map<String, Object> checkSqlMap, StringBuilder exLog, PrintResult printResult) {
        String sql = (String) checkSqlMap.get("sql");
        String dataSource = (String) checkSqlMap.get("dataSource");
        Map<String, String> condition = (Map<String, String>) checkSqlMap.get("condition");
        
        List result = resultHandleService.executeSql(sql, dataSource, exLog);
        
        String checkResult = this.checkResult(result, condition, printResult, Collections.EMPTY_MAP);
        scheduleTask.setFindTotal(1);
        // 发送消息到钉钉
        if (checkResult.length() > 1) {
            scheduleTask.setErrTotal(1);
        } else {
            scheduleTask.setErrTotal(0);
        }
        
        scheduleTask.setMessage(result.toString());
        // 处理结果 保存到es 
        resultHandleService.saveEsLog(null, scheduleTask, result, dataSource, sql, checkResult);
        return exLog;
    }
 
    private void unionDispose(List<Map> resultList, ScheduleTask scheduleTask, String sqlOne, Map<String, Object> checkSqlMap, StringBuilder exLog, PrintResult printResult) {
 
        int error = 0;
        String sql = (String) checkSqlMap.get("sql");
        String dataSource = (String) checkSqlMap.get("dataSource");
        Map<String, String> condition = (Map<String, String>) checkSqlMap.get("condition");
        List<String> paramList = new ArrayList<>();
        
        for (int i = 0, size = resultList.size(); i < size; i++) {
            Map param = resultList.get(i);
            String checkSql = this.buildCheckSql(param, sql);
            List result = resultHandleService.executeSql(checkSql, dataSource, exLog);
            String checkResult = this.checkResult(result, condition, printResult, param);
            
            if (checkResult.length() > 0) {
                error++;
                String string = param.values().toString();
                paramList.add(string);
            }
            // 处理结果 保存到es
            resultHandleService.saveEsLog(param, scheduleTask, result, dataSource, checkSql, checkResult);
        }
 
        int inputTotal = 0;
        if (resultList != null) {
            inputTotal = resultList.size();
        }
        scheduleTask.setFindTotal(inputTotal);
        scheduleTask.setErrTotal(error);
        String paramStr = paramList.toString();
        printResult.setSql(sql);
        scheduleTask.setMessage(paramStr);
 
    }
 
    private String checkResult(List result, Map<String, String> condition, PrintResult printResult, Map param) {
        String expression = condition.get("returnSize");
        
        StringBuilder errLog = new StringBuilder();
        boolean checkResult = ExpressionUtils.checkResult(result, expression);
        Map paramAndValue = new HashMap<>(1); 
        List<Map> errList = new ArrayList<>();
        if (checkResult) {
            String remove = condition.remove("returnSize");
            Set<Entry<String, String>> entrySet = condition.entrySet();
            for (int i = 0, len = result.size(); i < len; i++) {
                Map map = (Map) result.get(i);
                
                int count = 0;
                for (Entry<String, String> entry : entrySet) {
                    String key = entry.getKey();
                    Object object = map.get(key);
                    boolean checkFiled = ExpressionUtils.checkResult(object, entry.getValue());
                    if (!checkFiled) {
                        count ++;
                        errLog.append(key).append(" = ").append(object).append(" ");
                    }
                }
                if (count > 0) {
                    errList.add(map);
                    paramAndValue.put(param, errList);
                }
            }
            condition.put("returnSize", remove);
        } else {
            errList = result;
            paramAndValue.put(param, errList);
            errLog.append("returnSize").append(" = ").append(result.toString()).append("   ");
        }
        if (!paramAndValue.isEmpty()) {
            printResult.getResult().add(paramAndValue);
        }
        return errLog.toString();
    }
}