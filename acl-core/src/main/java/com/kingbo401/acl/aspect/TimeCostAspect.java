package com.kingbo401.acl.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class TimeCostAspect {
	private static final Logger  logger = LoggerFactory.getLogger(TimeCostAspect.class);
	@Around("execution(public * com.kingbo401.iceacl.manager.impl.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
		Object[] args = joinPoint.getArgs();
		Object rst = null;
		try {
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
	        String methodName = methodSignature.getDeclaringTypeName() + "." + methodSignature.getName();  
			long t1 = System.currentTimeMillis();
			rst = joinPoint.proceed(args);
			long t2 = System.currentTimeMillis();
			logger.info("method " + methodName + " time cost " + (t2 - t1) + " ms");
		} catch (Throwable e) {
			throw e;
		}
		return rst;
	}
}
