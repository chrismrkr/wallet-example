package com.example.wallet_example1.common.aop.sharding;


import com.example.wallet_example1.common.dataSource.impl.ThreadLocalShardKeyContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ShardKeyAspect {
    @Around("com.example.wallet_example1.common.aop.sharding.ShardKeyPointcut.shardKeyAnnotatedMethod()")
    public Object resolveShardKey(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long id = Arrays.stream(args)
                .filter(arg -> arg instanceof Long)
                .map(arg -> (Long) arg)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No Long Parameter Found for ShardKey"));
        try {
            ThreadLocalShardKeyContextHolder.setKey(id);
            return joinPoint.proceed();
        } finally {
            ThreadLocalShardKeyContextHolder.clear();
        }
    }
}
