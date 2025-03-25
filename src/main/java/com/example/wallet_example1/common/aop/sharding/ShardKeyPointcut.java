package com.example.wallet_example1.common.aop.sharding;

import org.aspectj.lang.annotation.Pointcut;

public class ShardKeyPointcut {
    @Pointcut("@annotation(com.example.wallet_example1.common.aop.sharding.EnableSharding)")
    public void shardKeyAnnotatedMethod() {}
}
