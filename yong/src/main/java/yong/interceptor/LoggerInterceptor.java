package yong.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;
import yong.controller.BaseController;

@Slf4j
public class LoggerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!request.getRequestURI().startsWith("/resources")) {
            
        
            log.debug("=================================== START ===============================");
            log.debug("Request URI \t : " + request.getRequestURI());
            
            if (request.getRequestURI().startsWith("/admin")) {
                // 현재 로그인한 사용자 정보 획득
                BaseController controller = (BaseController) ((HandlerMethod) handler).getBean();
    //            UserDto currAdmin = controller.getCurrAdmin();
            }
        }
        return super.preHandle(request, response, handler);
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (!request.getRequestURI().startsWith("/resources")) {
            log.debug("=================================== END ===============================");
        }
        super.postHandle(request, response, handler, modelAndView);
    }
}
