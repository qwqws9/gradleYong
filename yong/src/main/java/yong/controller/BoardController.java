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
public class BoardController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BoardService boardService;
    
    /**
     * 
     * 게시물 작성 / 수정 폼 이동
     *
     * @since 2020. 3. 3.
     * @author yong
     *
     * @param userSeq
     * @return
     */
    @RequestMapping({"/board/boardReg", "/board/boardMod"})
    public ModelAndView boardForm(BoardDto board) {
        
        ModelAndView mv = new ModelAndView();
        
        if (super.getRequestURI().endsWith("Reg")) {
            mv.addObject("mode", "reg");
        } else {
            board = this.boardService.boardSelect(String.valueOf(board.getBoardSeq()));
            mv.addObject("board", board);
            mv.addObject("mode", "update");
        }
        
        mv.setViewName("/board/boardForm");
        
        return mv;
    }
    
    /**
     * 
     * 게시물 등록 / 수정
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     */
    @RequestMapping({"/board/boardSave", "/board/boardUpdate"})
    public ModelAndView boardSave(BoardDto board) {
        ModelAndView mv = new ModelAndView();
        if (super.getRequestURI().endsWith("Save")) {
            this.boardService.boardSave(board);
            mv.setViewName("redirect:/");
        } else {
            // 수정
            this.boardService.boardUpdate(board);
            mv.setViewName("redirect:/");
        }
        
        return mv;
    }
    
    
    /**
     * 
     * 게시물 목록 조회
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     */
    @RequestMapping("/board/boardList")
    public ModelAndView boardList() {
        ModelAndView mv = new ModelAndView();
        
        mv.addObject("list", this.boardService.boardList());
        mv.setViewName("/index");
        return mv;
    }

    @RequestMapping("/board/getBoard/{boardSeq}")
    public ModelAndView getBoard(@PathVariable String boardSeq) {
        if (StringUtils.isEmpty(boardSeq)) { throw new BadRequestException("존재하지 않는 게시물입니다."); }
        
        ModelAndView mv = new ModelAndView();
        
        mv.addObject("board", this.boardService.boardSelect(boardSeq));
        mv.setViewName("/board/getBoard");
        return mv;
    }
}
