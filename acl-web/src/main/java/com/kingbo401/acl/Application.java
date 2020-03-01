 package com.kingbo401.acl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * spring启动类
 * @author kingbo401
 * @date 2019/07/20
 */
@SpringBootApplication
@EnableTransactionManagement 
@EnableAspectJAutoProxy(proxyTargetClass = true)
@MapperScan("com.kingbo401.acl.dao") 
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
