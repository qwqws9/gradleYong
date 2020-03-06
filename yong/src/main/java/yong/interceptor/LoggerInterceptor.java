package yong.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;
import yong.controller.BaseController;
import yong.dto.CtgMstDto;
import yong.mapper.CtgMapper;
import yong.service.CtgService;

@Slf4j
public class LoggerInterceptor extends HandlerInterceptorAdapter {

    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!request.getRequestURI().startsWith("/resources")) {
            
        
            log.debug("=================================== START ===============================");
            log.debug("Request URI \t : " + request.getRequestURI());
            
        }
        return super.preHandle(request, response, handler);
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (!request.getRequestURI().startsWith("/resources")) {
            log.debug("=================================== END ===============================");
            if(request.getRequestURI().startsWith("/ctg/ctgInput") ) {
                request.setAttribute("update", true);
            } else {
                request.setAttribute("update", false);
            }
        }
        
        super.postHandle(request, response, handler, modelAndView);
    }
}
