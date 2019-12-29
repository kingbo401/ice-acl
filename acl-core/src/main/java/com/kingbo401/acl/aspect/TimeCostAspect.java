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
		long t1 = System.currentTimeMillis();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();  
		try {
			rst = joinPoint.proceed(args);
		} catch (Throwable e) {
			throw e;
		} finally {
			long t2 = System.currentTimeMillis();
			long cost = t2 - t1;
			if (cost > 20) {
				logger.info("method " + methodName + " time cost " + cost + " ms");
			}
		}
		return rst;
	}
}
