package yong.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.common.Const;
import yong.dto.BoardDto;
import yong.dto.UserDto;
import yong.exception.BadRequestException;
import yong.service.BoardService;
import yong.service.CtgService;
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
public class BoardController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CtgService ctgService;

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

        // 게시물 등록시
        if (super.getRequestURI().endsWith("Reg")) {
            mv.addObject("mode", "reg");
        } else { // 수정시
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

        // 게시물 저장
        if (super.getRequestURI().endsWith("Save")) {
            this.boardService.boardSave(board);
            mv.setViewName("redirect:/");
        } else { // 수정
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
    public ModelAndView boardList(BoardDto board) {
        ModelAndView mv = new ModelAndView();

        // 페이징 처리 데이터
        board.setStartNum(0);
        board.setPageCount(Const.pageCount); // 몇개씩 조회할 것인지.

        mv.addObject("boardCount", this.boardService.selectBoardListCount());
        mv.addObject("list", this.boardService.boardList(board));
        mv.addObject("ctgSeq", board.getCtgSeq());
        mv.setViewName("/index");

        return mv;
    }

    /**
     * 
     * 게시물 Ajax를 이용한 무한스크롤
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @param board
     * @return
     */
    @RequestMapping("/board/boardListAddForm")
    public ModelAndView boardListAddForm(BoardDto board) {
        ModelAndView mv = new ModelAndView();

        board.setPageCount(Const.pageCount);

        mv.addObject("list", this.boardService.boardList(board));
        mv.setViewName("/board/boardListAddForm");

        return mv;
    }

    /**
     * 
     * 게시물 상세 조회
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @param boardSeq
     * @return
     */
    @RequestMapping("/board/getBoard/{boardSeq}")
    public ModelAndView getBoard(@PathVariable String boardSeq) {
        if (StringUtils.isEmpty(boardSeq)) { throw new BadRequestException("존재하지 않는 게시물입니다."); }

        ModelAndView mv = new ModelAndView();

        mv.addObject("board", this.boardService.boardSelect(boardSeq));
        mv.setViewName("/board/getBoard");

        return mv;
    }
}
