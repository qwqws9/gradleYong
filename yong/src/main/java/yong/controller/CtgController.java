package yong.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import yong.common.Result;
import yong.dto.BoardDto;
import yong.dto.CtgMstDto;
import yong.dto.UserDto;
import yong.entity.CtgMstEntity;
import yong.exception.BadRequestException;
import yong.jpa.CtgJpa;
import yong.jpa.CtgMstJpa;
import yong.mapper.CtgMapper;
import yong.service.BoardService;
import yong.service.CtgService;
import yong.service.UserService;

@Controller
@Slf4j
public class CtgController extends BaseController {
    
    @Autowired
    private CtgService ctgService;
    
    @Autowired
    private CtgMapper ctgMapper;
    
    @RequestMapping("/ctg/ctgListJson")
    @ResponseBody
    public Result ctgListJson() {
        
        List<CtgMstDto> ctgList = this.ctgService.ctgList(null);
        ObjectMapper om = new ObjectMapper();
        Result result = new Result();
        try {
            result.putInfo("ctgListJson", om.writeValueAsString(ctgList));
        } catch (JsonProcessingException e) {
            log.error("String -> Json 에러");
        }
        
        return result;
    }
    
    
    @RequestMapping("/ctg/ctgList")
    public ModelAndView ctgList() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("ctgAllList", this.ctgService.ctgList(null));
        mv.setViewName("/ctg/ctgList");

        return mv;
    }
    
    
    @RequestMapping("/layout/left")
    public ModelAndView ctgListLeft() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("ctgList", this.ctgService.ctgList("Y"));
        mv.setViewName("/layout/left");
        
        return mv;
    }
    
    @RequestMapping("/ctg/ctgInput")
    public ModelAndView ctgInput() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("update", true);
        mv.setViewName("/ctg/ctgInput");
        
        return mv;
    }
    
    @RequestMapping("/ctg/ctgSave")
    @ResponseBody
    public Result ctgSave(CtgMstDto ctgMst) {
        
        log.debug("===> {} " , ctgMst);
        
        Result result = this.ctgService.ctgMstSave(ctgMst);
        
        
        return result;
    }
}
