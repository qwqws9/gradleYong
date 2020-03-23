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
import yong.annotation.AccessUser;
import yong.common.Const;
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

/**
 * 
 * 클래스명: <code>CtgController</code><br/><br/>
 *
 * 카테고리 컨트롤러
 *
 * @since 2020. 3. 16.
 * @author yong
 *
 */
@Controller
@Slf4j
public class CtgController extends BaseController {

    @Autowired
    private CtgService ctgService;

    @Autowired
    private CtgMapper ctgMapper;

    @Autowired
    private BoardService boardService;

    /**
     * 
     * 하위 카테고리 Json 리턴
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @return
     */
    @RequestMapping("/ctg/ctgListJson")
    @ResponseBody
    public Result ctgListJson() {

        List<CtgMstDto> ctgList = this.ctgService.ctgList(Const.Y);
        ObjectMapper om = new ObjectMapper();
        Result result = new Result();
        try {
            result.putInfo("ctgListJson", om.writeValueAsString(ctgList));
        } catch (JsonProcessingException e) {
            log.error("String -> Json 에러");
        }

        return result;
    }
    
    /**
     * 
     * 카테고리 관리 리스트 (전시여부 상관없이 모두 조회, 관리자 전용)
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @return
     */
    
    @RequestMapping("/ctg/ctgList")
    public ModelAndView ctgList() {
        ModelAndView mv = new ModelAndView();

        mv.addObject("ctgAllList", this.ctgService.ctgList(Const.Y));
        mv.setViewName("/ctg/ctgList");

        return mv;
    }

    /**
     * 
     * 사용자에게 보여질 카테고리 리스트
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @return
     */
    @RequestMapping("/layout/left")
    public ModelAndView ctgListLeft() {
        ModelAndView mv = new ModelAndView();

        mv.addObject("ctgList", this.ctgService.ctgList(Const.N));
        mv.addObject("boardCount", this.boardService.selectBoardListCount());
        mv.setViewName("/layout/left");

        return mv;
    }

    /**
     * 
     * 카테고리 생성 폼
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @return
     */
    @AccessUser
    @RequestMapping("/ctg/ctgInput")
    public ModelAndView ctgInput() {
        ModelAndView mv = new ModelAndView();

        mv.addObject("update", true);
        mv.setViewName("/ctg/ctgInput");

        return mv;
    }

    /**
     * 
     * 카테고리 추가
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @param ctgMst
     * @return
     */
    @RequestMapping("/ctg/ctgSave")
    @ResponseBody
    public Result ctgSave(CtgMstDto ctgMst) {

        log.debug("===> {} " , ctgMst);
        Result result = this.ctgService.ctgMstSave(ctgMst);

        return result;
    }

    /**
     * 
     * 카테고리 마스터 리스트 조회 (게시물 등록시 사용)
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @return
     */
    @RequestMapping("/ctg/selectCtgMstList")
    public ModelAndView selectCtgMstList() {
        ModelAndView mv = new ModelAndView();

        mv.addObject("mst" , true);
        mv.addObject("ctgList", this.ctgService.selectCtgMstList());
        mv.setViewName("/ctg/ctgSelectBox");

        return mv;
    }

    /**
     * 
     * 카테고리 하위 리스트 조회 (게시물 등록시 사용)
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @param ctgMst
     * @return
     */
    @RequestMapping("/ctg/selectCtgList")
    public ModelAndView selectCtgList(CtgMstDto ctgMst) {
        ModelAndView mv = new ModelAndView();

        mv.addObject("ctgList", this.ctgService.selectCtgList(ctgMst));
        mv.setViewName("/ctg/ctgSelectBox");

        return mv;
    }
}
