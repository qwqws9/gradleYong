package yong.controller;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController {

    /**
     * request 객체를 반환함.
     */
    public HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * response 객체를 반환함.
     */
    public HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getResponse();
    }

    /**
     * request uri 정보를 반환함.
     * @return
     */
    public String getRequestURI() {
        return this.getRequest().getRequestURI();
    }

    /**
     * session 객체를 반환함.
     * @return
     */
    public HttpSession getSession() {
        return this.getRequest().getSession();
    }

    /**
     * 세션에 필요한 정보를 저장함.
     * @param key
     * @param o
     */
    public <T extends Serializable> void saveSession(String key, T o) {
        this.getSession().setAttribute(key, o);
    }

    /**
     * 세션에서 해당 정보를 반환함.
     * @param key
     * @return
     */
    public Object getSession(String key) {
        return this.getSession().getAttribute(key);
    }

    /**
     * 세션에서 해당 정보를 삭제 처리함.
     * @param key
     */
    public void removeSession(String key) {
        this.getSession().removeAttribute(key);
    }

    /**
     * session id 반환
     * @return
     */
    public String getSessionId() {
        String sessionId = this.getSession().getId();
        log.debug("sessionId =============> " + sessionId);
        return sessionId;
    }

    /**
     * 해당 쿠키의 값을 반환함.
     * @param name
     */
    public String getCookie(String name) {
        Cookie[] cookies = this.getRequest().getCookies();

        if (cookies != null) {
            for (Cookie c : this.getRequest().getCookies()) {
                if (StringUtils.equals(c.getName(), name)) {
                    return c.getValue();
                }
            }
        }

        return null;
    }

    /**
     * 해당 정보를 쿠키에 저장함.
     * @param response
     * @param name
     * @param value
     */
    public void saveCookie(String name, String value) {
        // 쿠키 처리
        Cookie cookie = new Cookie(name, value.replaceAll("(\r\n|\r|\n|\n\r)", ""));    // 쿠키저장시 엔터값은 삭제한다. http 응답분할 방지
        cookie.setHttpOnly(true);       // document.cookie 방지 처리
        cookie.setPath("/");
        this.getResponse().addCookie(cookie);
    }

    /**
     * 해당 쿠키값을 삭제함.
     * @param response
     * @param name
     */
    public void removeCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        this.getResponse().addCookie(cookie);
    }
}
