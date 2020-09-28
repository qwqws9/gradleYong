package yong.controller;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.annotation.AccessUser;
import yong.common.Const;
import yong.dto.BoardDto;
import yong.dto.CommentDto;
import yong.dto.UserDto;
import yong.exception.BadRequestException;
import yong.service.BoardService;
import yong.service.CommentService;
import yong.service.CtgService;
import yong.service.DuntokiService;
import yong.service.UserService;

/**
 * 
 * 클래스명: <code>BoardController</code><br/><br/>
 *
 * 게시물 조회/수정/삭제 
 *
 * @since 2020. 3. 16.
 * @author yong
 *
 */
@Controller
@Slf4j
public class DuntokiController extends BaseController {

    @Autowired
    private DuntokiService duntokiService;

    @RequestMapping("/teset/aaa/{server}/{mainId}")
    @ResponseBody
    public String kakaoLink(@PathVariable String server, @PathVariable String mainId, @RequestParam String name) {
        System.out.println(server);
        System.out.println(mainId);
        System.out.println(name);
        return name;
    }
}
