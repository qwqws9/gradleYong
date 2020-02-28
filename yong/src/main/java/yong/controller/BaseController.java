package yong.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.dto.BoardDto;
import yong.entity.BoardEntity;
import yong.service.BaseService;

@Controller
@Slf4j
public class BaseController {

    @Autowired
    private BaseService baseService;
    
    
    
    @RequestMapping("/aa")
    public ModelAndView index() throws Exception{
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/board/side");
        List<BoardDto> a=  baseService.test();
        log.debug("aaa {} ",a);
        return mv;
    }
    
    @RequestMapping("/bb")
    public ModelAndView index1() throws Exception{
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/board/hello");
        BoardEntity a=  baseService.test1();
        log.debug("seq {}", a.getBoardSeq());
        log.debug("aaa {} ",a);
        return mv;
    }
    
    @RequestMapping("/cc")
    public ModelAndView index2() throws Exception{
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/board/hello");
        baseService.test2();
//        log.debug("seq {}", a.getBoardSeq());
//        log.debug("aaa {} ",a);
        return mv;
    }
    
    @RequestMapping("/dd")
    public ModelAndView index3() throws Exception{
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/board/hello");
        baseService.test3();
//        log.debug("seq {}", a.getBoardSeq());
//        log.debug("aaa {} ",a);
        return mv;
    }
}
