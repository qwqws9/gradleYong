package yong.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.dto.BoardDto;
import yong.dto.UserDto;
import yong.exception.BadRequestException;
import yong.service.BoardService;
import yong.service.UserService;

@Controller
@Slf4j
public class CtgController extends BaseController {
    
    @RequestMapping("/ctg/ctgList")
    public void ctgList() {
        
    }
}
