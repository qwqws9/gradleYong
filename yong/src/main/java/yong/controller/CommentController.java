package yong.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.common.Result;
import yong.dto.CommentDto;
import yong.service.CommentService;


@Controller
@Slf4j
public class CommentController extends BaseController {
    
    @Autowired
    private CommentService commentService;

    @RequestMapping({"/comment/commentSave", "/comment/commentUpdate"})
    @ResponseBody
    public Result commentSave(CommentDto comment) {
        Result result = new Result();
        
        if (super.getRequestURI().endsWith("Save")) {
            log.debug("댓글 정보 ====> {}", comment);
            result = this.commentService.commentSave(comment);
        }
        
        return result;
    }
    
    @RequestMapping("/comment/commentList")
    public ModelAndView commentList(CommentDto comment) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("commentList", this.commentService.selectCommentList(comment));
        mv.addObject("commentChildList", this.commentService.selectCommentChildList(comment));
        mv.setViewName("/comment/commentForm");

        return mv;
    }
}
