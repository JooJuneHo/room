package com.sns.room.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j(topic = "UseTimeAop")
@Aspect
@Component
public class UseTimeAop {


    @Pointcut("execution(* com.sns.room.comment.controller.*.*(..))")
    private void CommentController() {
    }

    @Pointcut("execution(* com.sns.room.post.controller.*.*(..))")
    private void PostController() {
    }

    @Pointcut("execution(* com.sns.room.follow.controller.*.*(..))")
    private void FollowController() {
    }

    @Pointcut("execution(* com.sns.room.like.controller.*.*(..))")
    private void LikeController() {
    }

    @Pointcut("execution(* com.sns.room.user.controller.*.*(..))")
    private void UserController() {
    }


    @Around("CommentController() || PostController() || FollowController() || LikeController() || UserController()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long runTime = endTime - startTime;
            log.info("END : " + joinPoint + " " + runTime + "ms");
        }
    }
}
