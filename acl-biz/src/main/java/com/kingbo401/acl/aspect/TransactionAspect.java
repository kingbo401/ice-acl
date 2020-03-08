package com.kingbo401.acl.aspect;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionAspect {
	@Autowired
	private DataSource dataSource;
	
	@Bean("txManager")
	public DataSourceTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean("txAdvice")
	public TransactionInterceptor txAdvice(DataSourceTransactionManager txManager) {
		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		RuleBasedTransactionAttribute attribute = new RuleBasedTransactionAttribute(
				TransactionDefinition.PROPAGATION_REQUIRED,
				Collections.singletonList(new RollbackRuleAttribute(Throwable.class)));
		Map<String, TransactionAttribute> taMap = new HashMap<>();
		taMap.put("add*", attribute);
		taMap.put("update*", attribute);
		taMap.put("save*", attribute);
		taMap.put("insert*", attribute);
		taMap.put("delete*", attribute);
		taMap.put("remove*", attribute);
		taMap.put("freeze*", attribute);
		taMap.put("unfreeze*", attribute);
		taMap.put("batch*", attribute);
		taMap.put("grant*", attribute);
		taMap.put("revoke*", attribute);
		taMap.put("sync*", attribute);
		source.setNameMap(taMap);
		return new TransactionInterceptor(txManager, source);
	}
	
	@Bean
	public DefaultPointcutAdvisor defaultPointcutAdvisor(TransactionInterceptor txAdvice) {
		DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
		defaultPointcutAdvisor.setAdvice(txAdvice);
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(public * com.kingbo401.acl.manager.impl.*.*(..))");
		defaultPointcutAdvisor.setPointcut(pointcut);
		return defaultPointcutAdvisor;
	}
}
