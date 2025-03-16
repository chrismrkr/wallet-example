package com.example.wallet_example1.common.aop.aspect;

import com.example.wallet_example1.common.database.ShardRoutingDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ShardKeyAspect {

    @Pointcut("@annotation(UseShard)") // @UseShard 어노테이션이 있는 메소드만 적용
    public void shardPointcut() {}

    @Around("shardPointcut() && args(userId, ..)")
    public Object around(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
        try {
            String shardKey = determineShardKey(userId);
            ShardRoutingDataSource.setCurrentShard(shardKey); // 샤드 키 설정
            return joinPoint.proceed();
        } finally {
            ShardRoutingDataSource.clear(); // 요청이 끝나면 클리어
        }
    }

    private String determineShardKey(Long userId) {
        return (userId % 2 == 0) ? "shardA" : "shardB"; // 예제: userId의 짝수/홀수에 따라 샤드 결정
    }
}