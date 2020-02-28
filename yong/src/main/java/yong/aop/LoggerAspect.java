package yong.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LoggerAspect {
    // 포인트컷 표현식
    // execution(void board..insert*(..))  리턴타입이 void이고 board 패키지 하위의 모든 패키지중에서 insert로 시작하고 파라미터가 0개 이상인 메서드 호출
    // execution(* board..controller.*Controller.*(..)) 모든 리턴타입과 board 패키지 하위의 패키지중 controller 패키지에 Controller로 끝나는 클래스중
    //                                                  파라미터가 0개 이상인 모든 메서드 호출
    // execution(* board.controller.*()) board.controller 패키지 밑에 파라미터가 없는 모든 메서드 호출
    // execution(* board..select*(*)) board 패키지 하위의 모든 패키지중 select로 시작하고 파라미터가 한개인 모든 메서드 호출
    // execution(* board..select*(*,*)) board 패키지 하위의 모든 패키지중 select로 시작하고 파라미터가 두개인 모든 메서드 호출
    
    
    // within 특정 타입에 속하는 메서드를 포인트컷으로 설정
    // within(board.service.boardServiceImpl) board.service 패키지 밑에 있는 boardServiceImpl 클래스의 메서드가 호출될때
    // within(board.service.*ServiceImpl) board.service 패키지 밑에 있는 ServiceImpl이라는 이름으로 끝나는 메서드가 호출될떄
    
    // bean 스프링의 빈 이름의 패턴으로 포인트컷을 설정
    // bean(boardServiceImpl) boardServiceImpl 이라는 이름을 가진 빈의 메서드가 호출될때
    // bean(*ServiceImpl) ServiceImpl 이라는 이름으로 끝나는 빈의 메서드가 호출될때
    
    @Around("execution(* yong..controller.*Controller.*(..)) or execution(* yong..service.*Impl.*(..)) or execution(* yong..mapper.*Mapper.*(..))")
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        String type = "";
        String name = joinPoint.getSignature().getDeclaringTypeName();
        
        if(name.indexOf("Controller") > -1) {
            type = "Controller \t : ";
        }
        else if (name.indexOf("Service") > -1 ) {
            type = "Service \t : ";
        }
        else if (name.indexOf("Mapper") > -1 ) {
            type = "Mapper \t\t : ";
        }
        log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");
        return joinPoint.proceed();
    }
}