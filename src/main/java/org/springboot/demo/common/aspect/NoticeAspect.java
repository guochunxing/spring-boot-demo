package org.springboot.demo.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.stereotype.Component;

/**
 * 注解切面
 */
@Aspect
@Component
public class NoticeAspect {
    @Pointcut("@annotation(org.springboot.demo.common.aspect.NoticeAnnotation)")
    public void anyMethod() {
    }

    @Before(value = "anyMethod()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object aThis = joinPoint.getThis();
        Object target = joinPoint.getTarget();
        String kind = joinPoint.getKind();
        Signature signature = joinPoint.getSignature();
        SourceLocation sourceLocation = joinPoint.getSourceLocation();
        System.out.println("前置通知。。。" + aThis);
        System.out.println("前置通知。。。" + target);
        System.out.println("前置通知。。。" + kind); // method-execution
        System.out.println("前置通知。。。" + signature);
        System.out.println("前置通知。。。" + sourceLocation);
        System.out.println();
    }

    @After(value = "anyMethod()")
    public void doAfter(JoinPoint joinPoint) {
        JoinPoint.StaticPart staticPart = joinPoint.getStaticPart();
        System.out.println("后置通知。。。" + staticPart);
    }

    @AfterReturning(value = "anyMethod()", returning = "returnValue")
    public void doAfterReturning(JoinPoint joinPoint, Object returnValue) {
        JoinPoint.StaticPart staticPart = joinPoint.getStaticPart();
        System.out.println("doAfterReturning。。。" + staticPart);
        System.out.println("doAfterReturning。。。" + returnValue);
    }

    @AfterThrowing(pointcut = "@annotation(org.springboot.demo.common.aspect.ErrorNotice)", throwing = "throwable")
    public void errorSendEmail(Throwable throwable) {
        System.out.println(throwable.getMessage());
        System.out.println("这是一个发送警告通知的注解");
    }
}
