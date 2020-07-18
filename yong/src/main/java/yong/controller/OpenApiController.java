package yong.controller;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.annotation.AccessUser;
import yong.common.Const;
import yong.common.Result;
import yong.dto.BoardDto;
import yong.dto.CommentDto;
import yong.dto.UserDto;
import yong.exception.BadRequestException;
import yong.service.BoardService;
import yong.service.CommentService;
import yong.service.CtgService;
import yong.service.OpenApiService;
import yong.service.UserService;


/**
 * 클래스명: <code>OpenApiController</code><br/><br/>
 *
 * 설명을 기입하세요
 *
 * @since 2020. 4. 6.
 * @author yong
 *
 */
@Controller
@Slf4j
public class OpenApiController extends BaseController {

    @Autowired
    private OpenApiService openApiService;
    
    @RequestMapping("/openApi/museumList")
    public ModelAndView getMuseumList(ModelAndView mv) {

        mv.setViewName("/openApi/museumList");

        return mv;
    }
    
    @RequestMapping("/openApi/museumData")
    public ModelAndView getMuseumData(ModelAndView mv, String pageNo, String rows) {
        
        mv.addObject("list", this.openApiService.getMuseumList(pageNo, rows));
        mv.setViewName("/openApi/museumData");
        
        return mv;
    }
    
    @RequestMapping("/openApi/naverMap")
    public ModelAndView naverMap(ModelAndView mv) {
        
        mv.addObject("clientId", "15o2718p43");
        mv.setViewName("/openApi/naverMap");
        
        return mv;
    }
    
    @RequestMapping("/openApi/chargeMap")
    @ResponseBody
    public Result chargeMap(String addr) {
        
        if (StringUtils.isEmpty(addr)) {
            return new Result(this.openApiService.getChargeLocation());
        }

        return new Result(this.openApiService.getChargeLocation(addr));
    }
    
    @RequestMapping("/openApi/naverMap2")
    public ModelAndView naverMap2(ModelAndView mv) {
        
        mv.addObject("clientId", "15o2718p43");
        mv.setViewName("/openApi/naverMap2");
        
        return mv;
    }
    
    @RequestMapping("/aaa/bbbb")
    @ResponseBody
    public String test() {
        Document a = null;
        
        try {
             a = Jsoup.connect("http://dundam.xyz/searchActionTest.jsp?server=all&name=%EC%94%A8%EB%A6%AC%EC%95%BC").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36").validateTLSCertificates(false).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Elements e = a.select("b");
        
        StringBuilder sb = new StringBuilder();
        
        for (Element b : e) {
            sb.append(b.text());
        }

        //return new Result(sb.toString());
        return sb.toString();
    }
}
