package yong.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        
        List<CommentDto> parentComment = this.commentService.selectCommentList(comment);
        List<CommentDto> childComment = this.commentService.selectCommentChildList(comment);

        mv.addObject("commentList", parentComment);
        mv.addObject("commentChildList", childComment);
        
        ObjectMapper om = new ObjectMapper();
        try {
            mv.addObject("commentListJson", om.writeValueAsString(parentComment));
        } catch (JsonProcessingException e) {
            log.error("String -> Json 에러");
        }
        
        mv.setViewName("/comment/commentForm");

        return mv;
    }
}
