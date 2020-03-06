package yong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.common.Result;
import yong.service.BoardService;
import yong.service.CommonService;
import yong.service.CtgService;

@Controller
@Slf4j
public class CommonController {

    @Autowired
    private CommonService commonService;
    
    @Autowired
    private BoardService boardService;
    
    @Autowired
    private CtgService ctgService;
    
    @RequestMapping("/")
    public ModelAndView index() {
        // 나중에 사이드 메뉴 목록 가져오는 메서드 추가하기
        ModelAndView mv = new ModelAndView();
        
        mv.addObject("list", this.boardService.boardList());
        mv.addObject("ctgList", this.ctgService.ctgList("Y"));
        mv.setViewName("/index");

        return mv;
    }
    
    @RequestMapping("/common/popup/uploadFilePopup")
    public ModelAndView filePopup() {
        ModelAndView mv = new ModelAndView();
        
        mv.setViewName("/common/uploadFilePopup");
        return mv;
    }

    @RequestMapping("/common/uploadFile/{tempSeq}")
    @ResponseBody
    public Result fileUpload(MultipartHttpServletRequest request, @PathVariable String tempSeq) {
        
        Result result = commonService.fileUpload(request, tempSeq);

        return result;
    }
    
}
