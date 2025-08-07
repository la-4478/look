package com.lookmarket.common.log;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAdvice {
	private static final Logger logger = LoggerFactory.getLogger(LoggingAdvice.class);
	
	@Before("execution(* com.lookmarket..*ServiceImpl.*(..))")
	public void startLog(JoinPoint jp) {	
		logger.info("-----------------------------------------");
		logger.info("AOP Log: Method-Call-Before");
		
		logger.info("Target Method:" + jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName());
		
		logger.info("Parameters:" + Arrays.toString(jp.getArgs()));
	}
	
	@After("execution(* com.lookmarket..*ServiceImpl.*(..))")
	public void after(JoinPoint jp) {

	}
	
	@Around("execution(* com.lookmarket..*ServiceImpl.*(..))")
	public Object timeLog(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();
		
		Object result = pjp.proceed();
		
		long endTime = System.currentTimeMillis();
		
		logger.info("==============================");
		logger.info("AOP Log: Method-Execution-Time");
		logger.info(pjp.getSignature().getName() + " : " + (endTime - startTime) + "ms");
		logger.info("==============================");
		
		return result;
	}
}
