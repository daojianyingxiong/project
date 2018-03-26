package com.enlightent.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import com.enlightent.util.DingdingUtils;

@Aspect  
@Configuration
public class ExcuteLog {

	@Around("execution (* com.enlightent.service.ResultHandleService.executeSql(..))")
	public Object preProcessQueryPattern(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		StringBuilder exLog = (StringBuilder) args[2];
		
		Object proceed = pjp.proceed(args);
		if (exLog != null) {
			exLog.append("sql：").append(args[0]).append(DingdingUtils.NEW_LINE);
			String string = "";
			if (proceed != null) {
				string = proceed.toString();
				int length = string.length();
				if (length > 200) {
					string = string.substring(0, 200) + "...";
				}
			}
			exLog.append("执行结果：").append(string).append(DingdingUtils.NEW_LINE).append(DingdingUtils.NEW_LINE);
		}
		return proceed;

	}
}
