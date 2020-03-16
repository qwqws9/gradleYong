package yong.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.common.Result;
import yong.dto.UserDto;
import yong.exception.BadRequestException;
import yong.service.UserService;

/**
 * 
 * 클래스명: <code>UserController</code><br/><br/>
 *
 * 회원관리 컨트롤러
 *
 * @since 2020. 3. 16.
 * @author yong
 *
 */
@Controller
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 
     * 유저 등록 / 수정폼 이동
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     * @return
     */
    @RequestMapping({"/user/userReg", "/user/userMod"})
    public ModelAndView userForm(String userSeq) {

        ModelAndView mv = new ModelAndView();

        // 회원 등록
        if (super.getRequestURI().endsWith("Reg")) {
            mv.addObject("mode", "reg");
        } else { // 수정
            HttpSession session = super.getSession();
            UserDto user = (UserDto)session.getAttribute("userInfo");
            if (user == null) { throw new BadRequestException("세션이 만료되었습니다."); }
            if (StringUtils.isEmpty(userSeq) || user.getUserSeq() != Long.valueOf(userSeq)) { throw new BadRequestException("잘못된 요청입니다."); }
            mv.addObject("user", this.userService.getUser(user.getUserSeq()));
            log.info("user =>>> {}", user);
            mv.addObject("mode", "update");
        }

        mv.setViewName("/user/userForm");

        return mv;
    }

    /**
     * 
     * 유저 등록 / 수정
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     */
    @RequestMapping({"/user/userSave", "/user/userUpdate"})
    public ModelAndView userSave(UserDto user) {
        ModelAndView mv = new ModelAndView();
        log.debug("user = > {}" , user);

        // 회원 등록
        if (super.getRequestURI().endsWith("Save")) {
            this.userService.userSave(user);
            mv.setViewName("/user/loginForm");
        } else { // 수정
            this.userService.userMod(user);
            mv.setViewName("/index");
        }

        return mv;
    }

    /**
     * 
     * 유저 목록 조회
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     */
    @RequestMapping("/user/userList")
    public void userList() {
        
    }

    /**
     * 
     * 회원가입 폼 이동
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     * @return
     */
    @RequestMapping("/user/userLoginForm")
    public String userLoginForm() {
        return "/user/loginForm";
    }

    /**
     * 
     * 로그인 처리
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     * @param user
     * @return
     */
    @RequestMapping("/user/userLogin")
    @ResponseBody
    public Result userLogin(UserDto user) {
        if (user == null || StringUtils.isEmpty(user.getUserId()) || StringUtils.isEmpty(user.getUserPwd())) {
            return new Result(100, "아이디, 패스워드 오류");
        }

        log.debug("user => {} ", user);

        Result result = userService.login(user);

        // 로그인 성공시 세션에 데이터 저장
        if (result.getResultCode() == 1) {
            HttpSession session = super.getSession();
            session.setAttribute("userInfo", result.getInfo("data"));
        }

        return result;
    }
}
