package yong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.common.Const;
import yong.common.Result;
import yong.dto.BoardDto;
import yong.service.BoardService;
import yong.service.CommonService;
import yong.service.CtgService;

/**
 * 
 * 클래스명: <code>CommonController</code><br/><br/>
 *
 * 공통 컨트롤러
 *
 * @since 2020. 3. 16.
 * @author yong
 *
 */
@Controller
@Slf4j
public class CommonController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CtgService ctgService;

    /**
     * 
     * 인덱스 진입
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @param board
     * @return
     */
    @RequestMapping("/")
    public ModelAndView index(BoardDto board) {
        ModelAndView mv = new ModelAndView();
        board.setStartNum(0);
        board.setPageCount(Const.pageCount);

        mv.addObject("list", this.boardService.boardList(board));
        mv.setViewName("/index");

        return mv;
    }

    /**
     * 
     * 파일업로드 팝업창
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @return
     */
    @RequestMapping("/common/popup/uploadFilePopup")
    public ModelAndView filePopup() {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("/common/uploadFilePopup");
        return mv;
    }

    /**
     * 
     * 파일 업로드
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @param request
     * @param tempSeq
     * @return
     */
    @RequestMapping("/common/uploadFile/{tempSeq}")
    @ResponseBody
    public Result fileUpload(MultipartHttpServletRequest request, @PathVariable String tempSeq) {

        Result result = commonService.fileUpload(request, tempSeq);

        return result;
    }
}
