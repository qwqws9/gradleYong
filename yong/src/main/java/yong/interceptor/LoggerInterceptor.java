package yong.interceptor;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;
import yong.annotation.AccessUser;
import yong.controller.BaseController;
import yong.dto.CtgMstDto;
import yong.dto.UserDto;
import yong.exception.BadRequestException;
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
            
         // 컨트롤러에 대한 요청인 경우를 처리함. 일반 이미지나 HTML 요청은 스킵
            if (handler instanceof HandlerMethod) {
                Method method = ((HandlerMethod) handler).getMethod();
                // 관리자 권한 체크
                this.AdminAuthCheck(request, handler, method);
            }
            
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
    
    private void AdminAuthCheck(HttpServletRequest request, Object handler, Method method) {
        AccessUser adminAuthExcept = method.getAnnotation(AccessUser.class);
        
        if (adminAuthExcept != null) {
            BaseController controller = (BaseController) ((HandlerMethod) handler).getBean();
            UserDto user = controller.getCurrUser();
            if (user == null) { throw new BadRequestException("접근 권한 없음.."); }
            if (!"admin".equals(user.getUserRole())) { throw new BadRequestException("접근 권한 없음.."); }
        }
    }
}
