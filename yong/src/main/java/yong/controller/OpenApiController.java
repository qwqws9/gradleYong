package yong.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
    
    private static final String DUNDAM = "http://dundam.xyz/";
    private String URL;
    private static Map<String, String> SERVER;
    
    static {
        SERVER = new HashMap<String, String>();
        SERVER.put("카인", "cain");
        SERVER.put("디레지에", "diregie");
        SERVER.put("시로코", "siroco");
        SERVER.put("프레이", "prey");
        SERVER.put("카시야스", "casillas");
        SERVER.put("힐더", "hilder");
        SERVER.put("안톤", "anton");
        SERVER.put("바칼", "bakal");
    }

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
    
    /**
     * 던담 대미지 조회
     * @return
     */
    @RequestMapping("/dundam/damage/{server}/{name}")
    @ResponseBody
    public String dundamDamage(@PathVariable String server, @PathVariable String name) {
        Document doc = null;
        StringBuilder sb = new StringBuilder();
        
        try {
            String engServer = null;
            String deserver = URLDecoder.decode(server, "UTF-8");
            String dename = URLDecoder.decode(name, "UTF-8");
            System.out.println("!!!!!!" + server);
            System.out.println("!!!!!!" + deserver);
            
            if (SERVER.containsKey(deserver)) {
                engServer = SERVER.get(deserver);
            } else {
                return "서버명을 확인해주세요.";
            }
            
            this.URL = DUNDAM + "searchActionTest.jsp?server="+engServer+"&name="+name;
            doc = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
        
            if (doc.text().indexOf("점검중") > -1) { return "현재 점검중 입니다."; }
            if (doc.text().indexOf("없습니다.") > -1) { return "검색결과가 없습니다.\n 아이디를 확인해주세요."; }
            
            Elements e = doc.select("#equipment > tbody > tr:nth-child(1) > td:nth-child(1) > div.image_cut > a");
            
            for (Element b : e) {
                System.out.println(b);
                if (b.attr("href").indexOf("view.jsp") == -1) { continue; }
                sb.append(DUNDAM);
                sb.append(b.attr("href"));
            }
            
            doc = Jsoup.connect(sb.toString()).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
            String send = doc.select("#sendbag > table > tbody > tr:nth-child(14) > td").text(); // 샌드백
            String rogen = doc.select("#rogen > table > tbody > tr:nth-child(14) > td:nth-child(3)").text(); // 로젠 1시
            String siroco = doc.select("#siroco > table > tbody > tr:nth-child(14) > td:nth-child(3)").text(); // 시로코 1시
            String info = doc.select("body > div > div > div.col-sm-3 > div:nth-child(2) > p:nth-child(2) > font").text(); // 캐릭터 정보
            sb.setLength(0);
            sb.append(server + " / " + dename + "*emrms*");
            String[] infoArr = info.split("/");
            sb.append(infoArr[1] + " / " + infoArr[2].split(" ")[1] + "*emrms*");
            sb.append("&lt;샌드백&gt;");
            sb.append("*emrms*");
            sb.append(send);
            sb.append("*emrms*");
            sb.append("&lt;로젠 1시&gt;");
            sb.append("*emrms*");
            sb.append(rogen);
            sb.append("*emrms*");
            sb.append("&lt;시로코 1시&gt;");
            sb.append("*emrms*");
            sb.append(siroco);
            sb.append("*emrms*");
            
            
        } catch (IOException e) {
            return "조회 실패! \n 관리자에게 문의주세요.";
        }
        
        return sb.toString();
    }
}
