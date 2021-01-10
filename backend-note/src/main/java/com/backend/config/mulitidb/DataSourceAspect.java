package com.backend.config.mulitidb;

import com.backend.config.mulitidb.dprecated.DynamicDataSource;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value = 1)
@Slf4j
public class DataSourceAspect implements Ordered {

    /**
     * 切点: 所有配置 DataSource 注解的方法
     */
    @Pointcut("@annotation(com.backend.config.mulitidb.DbSource)")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            DbSource ds = method.getAnnotation(DbSource.class);

            // 通过判断 DataSource 中的值来判断当前方法应用哪个数据源
            MulitiDataSourceHolder.setDataSource(ds.value());
            log.info("current datasource is " + ds.value());

            return point.proceed();
        } finally {
            MulitiDataSourceHolder.clearDataSource();
            log.info("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
