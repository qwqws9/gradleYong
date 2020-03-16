package yong.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.exception.BadRequestException;
// 해당 클래스가 예외처리 클래스임을 알려줌.
@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    public ModelAndView badRequestHandler(HttpServletRequest request, BadRequestException exception) {
        
        log.info("장애발생 BadReqeustException(커스텀 예외)");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/error/error_default");
        mv.addObject("message", exception.getMessage());
        
        log.error("excetpion",exception);
        
        return mv;
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ModelAndView defaultExceptionHandler(HttpServletRequest request, Exception exception) {
        
        log.info("장애발생 Exception(커스텀 거치지않는 예외 ******* 추가)");
        ModelAndView mv = new ModelAndView("/error/error_default");
        mv.addObject("exception",exception);
        
        log.error("excetpion",exception);
        
        return mv;
    }
    
}