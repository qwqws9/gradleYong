package yong.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import yong.common.Result;
import yong.service.OpenApiService;


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
    
    private static final String DUNDAM = "https://dundam.xyz/";
    private static final String DUNTOKI = "http://duntoki.xyz/";
    private static final String NEOPLE = "https://api.neople.co.kr/df/";
    private static final String API_KEY = "NZsA1lAqj64UpeGK1XQxEfUU3PZUWOmw";
    private String defaultUrl;
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
    
    @RequestMapping("/test/image")
    public ModelAndView getMuseumData1(ModelAndView mv) {
        
        mv.setViewName("/img/test");
        
        return mv;
    }
    
    
    
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
     *  카카오 링크
     * @param server
     * @param name
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/neople/searchInfo/{server}")
    @ResponseBody
    public String kakaoLink(@PathVariable String server, @RequestParam String name) throws UnsupportedEncodingException {
        
        Document doc = null;
        StringBuilder sb = new StringBuilder();
        JSONObject obj = new JSONObject();
        obj.put("server", server);
        obj.put("name", name);
        String neopleServer = "";
        if (SERVER.containsKey(server)) {
            neopleServer = SERVER.get(server);
        } else {
            return "서버명을 확인해주세요.";
        }
        
        String chcId = this.getCharacterId(neopleServer,name);
        obj.put("img", "https://img-api.neople.co.kr/df/servers/" + neopleServer +"/characters/"+ chcId + "?zoom=1");
        obj.put("img2", "https://img-api.neople.co.kr/df/servers/" + neopleServer +"/characters/"+ chcId + "?zoom=3");
        
        
        try {
            String engServer = null;
            String deserver = URLDecoder.decode(server, "UTF-8");
            String dename = URLDecoder.decode(name, "UTF-8");
            System.out.println(server + " : " + deserver);
            System.out.println(name + " : " + dename);
            
            
            if (SERVER.containsKey(deserver)) {
                engServer = SERVER.get(deserver);
            } else {
                obj.put("msg", "서버명을 확인해주세요.");
                return obj.toJSONString();
            }
            
            String neopleId = this.getCharacterId(engServer, dename);
            
            this.defaultUrl = DUNDAM + "view?image="+ neopleId +"&server="+engServer+"&name="+name;
            doc = Jsoup.connect(defaultUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
            if (doc.text().indexOf("점검중") > -1) { obj.put("msg", "현재 점검중 입니다."); return obj.toJSONString(); }
            if (doc.text().indexOf("없습니다.") > -1) { obj.put("msg", "검색결과가 없습니다. 아이디를 확인해주세요."); return obj.toJSONString(); }
            
            String info = doc.select("body > div > div > div.col-sm-3 > div:nth-child(2) > p:nth-child(2) > font").text(); // 캐릭터 정보
            
            if (doc.text().indexOf("버프 계산") > -1) {
                String[] infoArr = info.split("/");
                String locA = "5";
                String locB = "5";
                String locC = "9";
                if (infoArr[1].indexOf("眞 크루세이더") > -1) {
                    locA = "6";
                    locB = "6";
                    locC = "11";
                } 
                // #Present > table > tbody > tr:nth-child(6) > td:nth-child(2)
                // #Present > table > tbody > tr:nth-child(6) > td:nth-child(4)
                // #Present > table > tbody > tr:nth-child(11) > td
                
                // 세라핌 헤카테
                // #Present > table > tbody > tr:nth-child(5) > td:nth-child(2)
                // #Present > table > tbody > tr:nth-child(5) > td:nth-child(4)
                // #Present > table > tbody > tr:nth-child(9) > td
                
                //doc.select("#myTab > li.active > a"); // 버프캐릭 확인용
                String a = doc.select("#Present > table > tbody > tr:nth-child("+locA+") > td:nth-child(2)").text(); // 힘 지능
                String b = doc.select("#Present > table > tbody > tr:nth-child("+locB+") > td:nth-child(4)").text(); // 물마독
                String c = doc.select("#Present > table > tbody > tr:nth-child("+locC+") > td").text(); // 버프 점수
                
                sb.setLength(0);
                
                sb.append(server + " / " + dename + "*emrms*");
                
                obj.put("rank", infoArr[1] + " - " + infoArr[2].split(" ")[1]);
                obj.put("rogen", "힘/지능 : " + a);
                obj.put("siroco", "물마독 : " + b);
                //obj.put("desc", infoArr[1] + " / " + infoArr[2].split(" ")[1] + " 힘/지능 : " + a + "물마독 : " + b + "버프점수 : " + c);
                
                sb.append(infoArr[1] + " / " + infoArr[2].split(" ")[1] + "*emrms*");
                sb.append("------------------------------");
                sb.append("*emrms*");
                sb.append("힘/지능  : " + a);
                sb.append("*emrms*");
                sb.append("물마독   : " + b);
                sb.append("*emrms*");
                sb.append("버프점수 : " + c);
                sb.append("*emrms*");
                sb.append("------------------------------");
                
            } else {
                String send = ""; // 샌드백
                String rogen = ""; // 로젠 1시
                String siroco = ""; // 시로코 1시
                rogen = doc.select("body > div.ct.con.container > div.tab-wrap > div.tab__content.damtab > div.damage > div > div:nth-child(16) > div.cc > table > tbody > tr:nth-child(16) > td:nth-child(3)").text(); // 로젠 1시
                send = doc.select("body > div.ct.con.container > div.tab-wrap > div.tab__content.damtab > div.damage > div > div:nth-child(15) > div.cc > table > tbody > tr:nth-child(13) > td:nth-child(2)").text();
                siroco = doc.select("body > div.ct.con.container > div.tab-wrap > div.tab__content.damtab > div.damage > div > div:nth-child(18) > div.csw > table.adamage > tbody > tr > td > div").text();
                String job = doc.select("body > div.c.con.container > div > div.in > li.job").text();
                String rank = doc.select("body > div.c.con.container > div > div.icr > ul > li > con").text();
                String totalRank = doc.select("body > div.c.con.container > div > div.icr > ul > li > span").text();
                
                String[] infoArr = totalRank.split(" ");
                obj.put("rank", job + " - (" + rank + "/" + infoArr[1] +"위");
                obj.put("rogen", "로젠 1시 : " + numberToKorean(rogen));
                obj.put("siroco", "시로코 1시 : " + numberToKorean(siroco));
                
            }
            
        } catch (IOException e) {
            //return "조회 실패! *emrms* 관리자에게 문의주세요.";
        }
        
        if (!obj.containsKey("msg")) {
            obj.put("msg", "ok");
        }
        
        
        return obj.toJSONString();
    }
    
    
    /**
     *  카카오 링크 (NEW)
     * @param server
     * @param name
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/neople/searchInfo/{server}/{name}/{encName}")
    @ResponseBody
    public String kakaoLink2(@PathVariable String server, @PathVariable String name, @PathVariable String encName) throws UnsupportedEncodingException {
        
        Document doc = null;
        StringBuilder sb = new StringBuilder();
        JSONObject obj = new JSONObject();
        obj.put("server", server);
        obj.put("name", name);
        obj.put("img", "https://img-api.neople.co.kr/df/servers/" + server +"/characters/"+ name + "?zoom=1");
        obj.put("img2", "https://img-api.neople.co.kr/df/servers/" + server +"/characters/"+ name + "?zoom=3");
        try {
//            encName = encodeURIComponent(encName);
            this.defaultUrl = DUNDAM + "view?image="+ name +"&server="+server+"&name="+encName;
            doc = Jsoup.connect(defaultUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
            if (doc.text().indexOf("점검중") > -1) { obj.put("msg", "현재 점검중 입니다."); return obj.toJSONString(); }
            if (doc.text().indexOf("없습니다.") > -1) { obj.put("msg", "검색결과가 없습니다. 아이디를 확인해주세요."); return obj.toJSONString(); }
            
            String info = doc.select("body > div > div > div.col-sm-3 > div:nth-child(2) > p:nth-child(2) > font").text(); // 캐릭터 정보
            String mainjob = doc.select("body > div.c.con.container > div > div.in > li.job").text();
            if (doc.text().indexOf("버퍼랭킹") > -1) {
                String locA = "5";
                String locB = "5";
                String locC = "9";
                boolean hekate = false;
                if (mainjob.indexOf("眞 인챈") > -1) {
                    locA = "6";
                    locB = "7";
                    hekate = true;
                } else if (mainjob.indexOf("眞") > -1) {
                    locA = "7";
                    locB = "8";
                }
                // #Present > table > tbody > tr:nth-child(6) > td:nth-child(2)
                // #Present > table > tbody > tr:nth-child(6) > td:nth-child(4)
                // #Present > table > tbody > tr:nth-child(11) > td
                
                // 세라핌 헤카테
                // #Present > table > tbody > tr:nth-child(5) > td:nth-child(2)
                // #Present > table > tbody > tr:nth-child(5) > td:nth-child(4)
                // #Present > table > tbody > tr:nth-child(9) > td
                
                //doc.select("#myTab > li.active > a"); // 버프캐릭 확인용
                String a = doc.select("body > div.ct.con.container > div.tab-wrap > div:nth-child(27) > div.cc.buffcal > table > tbody > tr:nth-child("+locA+") > td:nth-child(2) > div").text(); // 힘 지능
                String b = doc.select("body > div.ct.con.container > div.tab-wrap > div:nth-child(27) > div.cc.buffcal > table > tbody > tr:nth-child("+locA+") > td:nth-child(3) > div").text(); // 물마독
                String c = doc.select("body > div.ct.con.container > div.tab-wrap > div:nth-child(27) > div.cc.buffcal > table > tbody > tr:nth-child("+locB+") > td:nth-child(2)").text(); // 버프 점수
                
                //랭킹
                String rank = doc.select("body > div.c.con.container > div > div.icr > ul > li:nth-child(2) > con").text();
                //총랭킹
//                String totalRank = doc.select("body > div.c.con.container > div > div.icr > ul > li:nth-child(2) > span").text();
                
//                String[] infoArr2 = totalRank.split(" ");
                sb.setLength(0);
                
//                sb.append(server + " / " + dename + "*emrms*");
                
                if (hekate) {
                    String d[] = c.split("[(]");
                    obj.put("total", "버프력:" + d[0]);
                } else {
                    obj.put("total", "버프력:" + c);
                }
//                obj.put("rank", mainjob + " - (" + rank + "/" + infoArr2[1] +"위");
                obj.put("rank", mainjob + " - " + rank +"위");
                obj.put("rogen", "힘/지능 : " + a);
                obj.put("siroco", "물마독 : " + b);
                
                obj.put("kind", "buf");
            } else {
                String rogen = ""; // 로젠 1시
                String siroco = ""; // 시로코 1시
                rogen = doc.select("body > div.ct.con.container > div.tab-wrap > div.tab__content.damtab > div.damage > div > div:nth-child(16) > div.csw > table.adamage > tbody > tr > td > div").text(); // 로젠 1시
                siroco = doc.select("body > div.ct.con.container > div.tab-wrap > div.tab__content.damtab > div.damage > div > div:nth-child(18) > div.csw > table.adamage > tbody > tr > td > div").text();
                String job = doc.select("body > div.c.con.container > div > div.in > li.job").text();
                String rank = doc.select("body > div.c.con.container > div > div.icr > ul > li > con").text();
//                String totalRank = doc.select("body > div.c.con.container > div > div.icr > ul > li > span").text();
                
//                String[] infoArr = totalRank.split(" ");
//                obj.put("rank", job + " - (" + rank + "/" + infoArr[1] +"위");
                obj.put("rank", job + " - " + rank +"위");
                obj.put("rogen", "로젠 1시 : " + numberToKorean(rogen));
                obj.put("siroco", "시로코 1시 : " + numberToKorean(siroco));
                obj.put("kind", "deal");
                //obj.put("desc", infoArr[1] + " / " + infoArr[2].split(" ")[1] + " 로젠 1시 : " + numberToKorean(rogen) + "시로코 1시 : " + numberToKorean(siroco));
            }
            
        } catch (IOException e) {
            //return "조회 실패! *emrms* 관리자에게 문의주세요.";
        }
        
        if (!obj.containsKey("msg")) {
            obj.put("msg", "ok");
        }
        
        return obj.toJSONString();
    }
    
    /**
     * 던담 대미지 조회
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping("/dundam/damage/{server}")
    @ResponseBody
    public String dundamDamage(@PathVariable String server, @RequestParam String name) throws UnsupportedEncodingException {
        Document doc = null;
        StringBuilder sb = new StringBuilder();
        
        try {
            String engServer = null;
            String deserver = URLDecoder.decode(server, "UTF-8");
            String dename = URLDecoder.decode(name, "UTF-8");
            System.out.println(server + " : " + deserver);
            System.out.println(name + " : " + dename);
            
            
            if (SERVER.containsKey(deserver)) {
                engServer = SERVER.get(deserver);
            } else {
                return "서버명을 확인해주세요.";
            }
            
            String neopleId = this.getCharacterId(engServer, dename);
            System.out.println(neopleId);
//            this.defaultUrl = DUNDAM + "searchActionTest.jsp?server="+engServer+"&name="+name;
            this.defaultUrl = DUNDAM + "view?image="+ neopleId +"&server="+engServer+"&name="+name;
            System.out.println(defaultUrl);
            doc = Jsoup.connect(defaultUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
        
            if (doc.text().indexOf("점검중") > -1) { return "현재 점검중 입니다."; }
            if (doc.text().indexOf("없습니다.") > -1) { return "검색결과가 없습니다. *emrms* 아이디를 확인해주세요."; }
            
            Elements e = doc.select("#equipment > tbody > tr:nth-child(1) > td:nth-child(1) > div.image_cut > a");
            
            for (Element b : e) {
                System.out.println(b);
                if (b.attr("href").indexOf("view.jsp") == -1) { continue; }
                sb.append(DUNDAM);
                sb.append(b.attr("href"));
            }
            
//            doc = Jsoup.connect(sb.toString()).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
            
            String info = doc.select("body > div.c.con.container > div > div.in > li.job").text(); // 캐릭터 정보
            
            if (doc.text().indexOf("버프 계산") > -1) {
                String[] infoArr = info.split("/");
                String locA = "5";
                String locB = "5";
                String locC = "9";
                if (infoArr[1].indexOf("眞 인챈") > -1) {
                    locA = "6";
                    locB = "6";
                    locC = "13";
                } else if (infoArr[1].indexOf("眞") > -1) {
                    locA = "7";
                    locB = "7";
                    locC = "13";
                }
                //doc.select("#myTab > li.active > a"); // 버프캐릭 확인용
                String a = doc.select("#Present > table > tbody > tr:nth-child("+locA+") > td:nth-child(2)").text(); // 힘 지능
                String b = doc.select("#Present > table > tbody > tr:nth-child("+locB+") > td:nth-child(4)").text(); // 물마독
                String c = doc.select("#Present > table > tbody > tr:nth-child("+locC+") > td").text(); // 버프 점수
                sb.setLength(0);
                
                sb.append(server + " / " + dename + "*emrms*");
               
                sb.append(infoArr[1] + " / " + infoArr[2].split(" ")[1] + "*emrms*");
                sb.append("------------------------------");
                sb.append("*emrms*");
                sb.append("힘/지능  : " + a);
                sb.append("*emrms*");
                sb.append("물마독   : " + b);
                sb.append("*emrms*");
                sb.append("버프점수 : " + c);
                sb.append("*emrms*");
                sb.append("------------------------------");
                
            } else {
                String send = ""; // 샌드백
                String rogen = ""; // 로젠 1시
                String siroco = ""; // 시로코 1시
                
                //Elements AAA = doc.select("body > div.ct.con.container > div.tab-wrap > div.tab__content.damtab > div.damage > div > div:nth-child(16) > div.cc > table > tbody > tr:nth-child(16) > td:nth-child(3)"); // 로젠 1시
                rogen = doc.select("body > div.ct.con.container > div.tab-wrap > div.tab__content.damtab > div.damage > div > div:nth-child(16) > div.cc > table > tbody > tr:nth-child(16) > td:nth-child(3)").text(); // 로젠 1시
                send = doc.select("body > div.ct.con.container > div.tab-wrap > div.tab__content.damtab > div.damage > div > div:nth-child(15) > div.cc > table > tbody > tr:nth-child(13) > td:nth-child(2)").text();
                siroco = doc.select("body > div.ct.con.container > div.tab-wrap > div.tab__content.damtab > div.damage > div > div:nth-child(18) > div.csw > table.adamage > tbody > tr > td > div").text();
                System.out.println(send);
                System.out.println(rogen);
                System.out.println(siroco);
                
                String job = doc.select("body > div.c.con.container > div > div.in > li.job").text();
                String rank = doc.select("body > div.c.con.container > div > div.icr > ul > li > con").text();
                String totalRank = doc.select("body > div.c.con.container > div > div.icr > ul > li > span").text();
                
                String[] infoArr = totalRank.split(" ");
                System.out.println(job + " - (" + rank + "/" + infoArr[1] +"위");
//                for (Element ele : AAA) {
//                    if (ele.select("th").text().indexOf("총 딜") == -1) { continue; }
//                    rogen = ele.select("td:nth-child(3)").text();
//                }
//                
//                AAA = doc.select("#sendbag > table > tbody > tr");
//                for (Element ele : AAA) {
//                    if (ele.select("th").text().indexOf("총 딜") == -1) { continue; }
//                    send = ele.select("td:nth-child(2)").text();
//                }
//                
//                AAA = doc.select("#siroco > table > tbody > tr");
//                for (Element ele : AAA) {
//                    if (ele.select("th").text().indexOf("총 딜") == -1) { continue; }
//                    siroco = ele.select("td:nth-child(3)").text();
//                }
                
                sb.setLength(0);
                sb.append(server + " / " + dename + "*emrms*");
//                String[] infoArr = info.split("/");
//                sb.append(infoArr[1] + " / " + infoArr[2].split(" ")[1] + "*emrms*");
                sb.append("------------------------------");
                sb.append("*emrms*");
                sb.append("&lt;샌드백&gt;");
                sb.append("*emrms*");
                sb.append(numberToKorean(send));
                sb.append("*emrms*");
                sb.append("&lt;로젠 1시&gt;");
                sb.append("*emrms*");
                sb.append(numberToKorean(rogen));
                sb.append("*emrms*");
                sb.append("&lt;시로코 1시&gt;");
                sb.append("*emrms*");
                sb.append(numberToKorean(siroco));
                sb.append("*emrms*");
                sb.append("------------------------------");
            }
            
        } catch (IOException e) {
            return "조회 실패! *emrms* 관리자에게 문의주세요.";
        }
        
        return sb.toString();
    }
    
    @RequestMapping("/duntoki/{server}")
    @ResponseBody
    public String duntoki(@PathVariable String server, @RequestParam String name) throws UnsupportedEncodingException {
        
        if(!SERVER.containsKey(server)) {
            return "서버명을 확인해주세요.";
        }
        
        StringBuilder sb = new StringBuilder();
        
        try {
            Document doc = null;
            String encName = encodeURIComponent(name);
            System.out.println(name);
            this.defaultUrl = DUNTOKI + "giraffe?serverNm="+server+"&charNm="+encName;
            doc = Jsoup.connect(defaultUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
            if (doc.text().indexOf("존재하지 않는 캐릭터") > -1) { return "검색결과가 없습니다. *emrms* 아이디를 확인해주세요.";}
            
            sb.append("[" + server + " / " + name + "] *emrms*");
            sb.append("------------------------------"); sb.append("*emrms*");
            String a1 = doc.select("body > center:nth-child(1) > div.informationDiv > table.giraffeScore > tbody > tr > td:nth-child(2)").text(); // 기린점수
            String a2 =doc.select("body > center:nth-child(1) > div.informationDiv > table.giraffeScore > tbody > tr > td:nth-child(1)").text(); // 변동사항
            String a3 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo2 > tbody > tr > td:nth-child(1)").text(); // 첫 신화
            String a4 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo2 > tbody > tr > td:nth-child(2)").text(); // 신화 갯수
            String a5 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo2 > tbody > tr > td:nth-child(3)").text(); // 산물
            String a6 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo3 > tbody > tr > td:nth-child(3)").text(); // 잔향
            String a7 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo3 > tbody > tr > td:nth-child(2)").text(); // 시로코 골카
            String a8 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo3 > tbody > tr > td:nth-child(1)").text(); // 에픽도감 달성률
            String a9 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo4 > tbody > tr > td:nth-child(1)").text(); // 던생 최고의날
            String a10 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo4 > tbody > tr > td:nth-child(2)").text(); // 최고의날 획득 개수
//            String a11 = doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo1 > tbody > tr > td:nth-child(1)").text(); // 획득 에픽 전체 수
            String a12 = doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo1 > tbody > tr > td:nth-child(2)").text(); // 획득 에픽 지혜인도
            String a13 = doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo1 > tbody > tr > td:nth-child(3)").text(); // 획득 에픽 기타경로
            
            String temp = a12.split("개")[0];
            int b12 = 0;
            if (NumberUtils.isNumber(temp)) {
                b12 = Integer.parseInt(temp) * 10;
            }
            
            sb.append("기린점수 : " + a1); sb.append("*emrms*");
            sb.append(a2); sb.append("*emrms*");
            sb.append("첫 신화 : " + a3); sb.append("*emrms*");
            if (a4.indexOf("0") == -1) {
                sb.append("획득한 신화 : " + a4); sb.append("*emrms*");
            }
            if (b12 != 0) {
                sb.append("지혜인도 근사값 : 약" + b12 + "회"); sb.append("*emrms*");
            }
            sb.append("획득한 산물 : " + a5); sb.append("*emrms*");
            sb.append("잔향 획득여부 : " + a6); sb.append("*emrms*");
            sb.append("시로코 골카 : " + a7); sb.append("*emrms*");
            sb.append("던생 최고의 날 : " + a9); sb.append("*emrms*");
            sb.append("최고의 날 획득 수 : " + a10); sb.append("*emrms*");
            sb.append("*emrms*");
            sb.append("-----------획득경로-----------"); sb.append("*emrms*");
            sb.append("지혜의 인도 : " + a12); sb.append("*emrms*");
            sb.append("기타 경로 : " + a13); sb.append("*emrms*");
            sb.append("------------------------------"); sb.append("*emrms*");
            sb.append("에픽도감 달성률 : " + a8); sb.append("*emrms*");
            sb.append("------------------------------");
        } catch(Exception e) {
            return "조회 실패! *emrms* 관리자에게 문의주세요.";
        }
        
        return sb.toString();
    }
    

    @SuppressWarnings("unchecked")
    @RequestMapping("/neople/equip/{server}")
    @ResponseBody
    public String neopleApi(@PathVariable String server, @RequestParam String name, @RequestParam(required = false, defaultValue = "N") String imgYn) throws UnsupportedEncodingException {
        
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        JSONObject imgObj = new JSONObject();
        imgObj.put("server", server);
        try {
            if (SERVER.containsKey(server)) {
                server = SERVER.get(server);
            } else {
                return "서버명을 확인해주세요.";
            }
            
            String chcId = this.getCharacterId(server,name);
            if ("x".equals(chcId)) { return "존재하지 않는 캐릭터 입니다."; }
            
            URL url = new URL(NEOPLE +"servers/" + server +"/characters/"+ chcId +"/equip/equipment?apikey="+API_KEY);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            conn.setRequestMethod("GET");
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            
            imgObj.put("id", name);
            //String chcId = this.getCharacterId(server,name);
            imgObj.put("img", "https://img-api.neople.co.kr/df/servers/" + server +"/characters/"+ chcId + "?zoom=1");
            
            JSONParser parse = new JSONParser();
            JSONObject obj = (JSONObject)parse.parse(sb.toString());
            sb.setLength(0);
            sb.append(name +" 의 정보.."); sb.append("*emrms*");sb.append("*emrms*");sb.append("*emrms*");
            sb.append("길드명 : ");
            sb.append(obj.get("guildName").toString()); sb.append("*emrms*"); // 길드명
            sb.append("직업 : ");
            sb.append(obj.get("jobGrowName").toString()); // 직업명
            sb.append(" | ");
            sb.append(obj.get("jobName").toString()); sb.append("*emrms*"); // 기초직업명
            sb.append("모험단 명 : ");
            sb.append(obj.get("adventureName").toString()); sb.append("*emrms*");sb.append("*emrms*"); // 모험단 명
            
            // 장착 장비 세팅
            JSONArray jsonArr = (JSONArray) obj.get("equipment");
            for (int i = 0; i < jsonArr.size(); i++) {
                JSONObject tempObj = (JSONObject) jsonArr.get(i);
                sb.append(tempObj.get("slotName").toString()); // 타입
                sb.append(" : ");
                sb.append(tempObj.get("itemName")); // 이름
                sb.append(" ( +");
                sb.append(tempObj.get("reinforce")); // 강화/증폭 수치
                sb.append(" ");
                sb.append(tempObj.get("amplificationName") == null ? " 강화" : tempObj.get("amplificationName").toString());// 증폭 명
                if ("무기".equals(tempObj.get("slotName").toString())) { sb.append(" / "+tempObj.get("refine") + " 재련"); } // 재련
                sb.append(" )");
                sb.append("*emrms*");
                if (tempObj.containsKey("enchant")) {
                    JSONObject tempObj2 = (JSONObject) tempObj.get("enchant");
                    JSONArray jr = (JSONArray) tempObj2.get("status");
                    sb.append("[ ");
                    for (int j = 0; j < jr.size(); j++) {
                        JSONObject enchantObj = (JSONObject) jr.get(j);
                        sb.append(enchantObj.get("name").toString()); // 마부 명
                        sb.append(" +"+enchantObj.get("value").toString()); // 마부 수치
                        if (j != jr.size() -1 ) { sb.append(" / "); }
                    }
                    sb.append(" ]");
                } else {
                    sb.append("[ 마부 없음.. ]");
                }
                sb.append("*emrms*");
                sb.append("*emrms*");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    
                }
            }
        }
        
        return imgYn == "N" ? sb.toString() : imgObj.toJSONString();
    }
    
    @RequestMapping("/neople/img/{server}")
    @ResponseBody
    public String neopleApi3(@PathVariable String server, @RequestParam String name) throws UnsupportedEncodingException {
        
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            if (SERVER.containsKey(server)) {
                server = SERVER.get(server);
            } else {
                return "서버명을 확인해주세요.";
            }
            
            String chcId = this.getCharacterId(server,name);
            if ("x".equals(chcId)) { return "존재하지 않는 캐릭터 입니다."; }
            
            // https://img-api.neople.co.kr/df/servers/<serverId>/characters/<characterId>?zoom=1
            String imgUrl = "https://img-api.neople.co.kr/df/servers/" + server +"/characters/"+ chcId +"zoom=1";
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    
                }
            }
        }
        
        return sb.toString();
    }
    
    // 던파 API 인코딩 메서드
    public String encodeURIComponent(String component)   {     
        String result = null;      

        try {       
            result = URLEncoder.encode(component, "UTF-8");
        } catch (Exception e) {       
            result = component;     
        }      

        return result;   
    }
    
    // 캐릭터 고유 아이디 조회
    public String getCharacterId(String server, String name) {
        
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        
        try {
            
            name = this.encodeURIComponent(name);
            
            String a = NEOPLE + "servers/" + server + "/characters?characterName="+ name +"&limit=10&wordType=match&apikey="+ API_KEY;
            
            
            URL url = new URL(a);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            conn.setRequestMethod("GET");
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            JSONParser parse = new JSONParser();
            JSONObject obj = (JSONObject)parse.parse(sb.toString());
            JSONArray jsonArr = (JSONArray) obj.get("rows");
            if(!obj.get("rows").toString().trim().equals("[]")) {
                obj = (JSONObject) jsonArr.get(0);
                sb.setLength(0);
                sb.append(obj.get("characterId").toString());
            } else {
                sb.setLength(0);
                return "x";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    
                }
            }
        }
        
        
        
        return sb.toString();
    }
    
    // 아이템 검색
    @RequestMapping("/neople/searchItem")
    @ResponseBody
    public String neopleApi2(@RequestParam String searchType, @RequestParam String itemName) throws UnsupportedEncodingException {
        
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            
            String encItemName = this.encodeURIComponent(itemName);
            String itemId = "";
            
            // searchType : 1 아이템 검색용 // 2 : 경매장 검색용 ( 검색결과가 하나만 나와야함 )
            String trade = searchType == "1" ? "true" : "false";
            
            URL url = new URL(NEOPLE + "items?itemName=" + encItemName +"&q=trade:" + trade + "&limit=200&wordType=front&apikey=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            conn.setRequestMethod("GET");
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            JSONParser parse = new JSONParser();
            JSONObject obj = (JSONObject)parse.parse(sb.toString());
            if(!obj.get("rows").toString().trim().equals("[]")) {
                sb.setLength(0);
                JSONArray arr = (JSONArray) obj.get("rows");
                sb.append(itemName); sb.append("*emrms*");
                sb.append("검색결과 총 :" + arr.size() + "개");
                sb.append("*emrms*"); sb.append("*emrms*");
                for (int i = 0; i < arr.size(); i++ ) {
                    JSONObject itemList = (JSONObject)arr.get(i);
                    if (arr.size() == 1) { itemId = itemList.get("itemId").toString(); } // 한개만 검색되었을 경우에만 경매장 검색이동위해 처리
                    
                    sb.append(itemList.get("itemName").toString()); sb.append(" (");
                    sb.append(itemList.get("itemRarity").toString()); sb.append(")"); sb.append("*emrms*");
                    String itemType = itemList.get("itemType").toString(); // 타입
                    if ("스태커블".equals(itemType)) {
                        sb.append(itemList.get("itemTypeDetail").toString()); sb.append("*emrms*");
                    } else {
                        sb.append(itemType);
                        sb.append(" - ");
                        sb.append(itemList.get("itemTypeDetail").toString()); sb.append("*emrms*");
                    }
                    sb.append("사용가능레벨 : ");
                    sb.append(itemList.get("itemAvailableLevel").toString()); 
                    
                    if (i != arr.size() -1) {sb.append("*emrms*"); sb.append("*emrms*");}
                }
                
            } else {
                sb.setLength(0);
                return "검색결과가 없습니다.";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    
                }
            }
        }
        
        return sb.toString();
    }
    
    
    
    
    public static String numberToKorean(String number){
        number = number.replaceAll(",", "");
        String[] unitWords    = {"만", "억", "조", "경"};

        Stack<String> stack = new Stack<String>();
        int count = 0;
        int count2 = 0;
        for (int i = number.length()-1; i >= 0; i--) {
            
            stack.push(number.charAt(i)+"");
            count2++;
            if (count2 % 4 == 0 && i != 0) {
                stack.push(unitWords[count]);
                count++;
            }
        }
        StringBuilder sb = new StringBuilder();
        
        while(!stack.empty()) {
            sb.append(stack.pop());
        }
        
        return sb.toString();
    }
    
    
    
    
    
    public String replaceStr(String str) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("!",  "%21");
        map.put("|",  "%7C");
        map.put("\"", "%22");
        map.put("#",  "%23"); 
        map.put("$",  "%24"); 
        map.put("%",  "%25"); 
        map.put("&",  "%26"); 
        map.put("(",  "%28"); 
        map.put("}",  "%7D");
        map.put("~",  "%7E");
        map.put("￠", "%A2");
        map.put("￡", "%A3");
        map.put("￥", "%A5");
        map.put(")" , "%29");
        map.put("." ,  "%2E");
        map.put("/" ,  "%2F");
        map.put(":" ,  "%3A");
        map.put(";" ,  "%3B");
        map.put("<" ,  "%3C");
        map.put(">" ,  "%3E");
        map.put("=" ,  "%3D");
        map.put("?" ,  "%3F");
        map.put("@" ,  "%40");
        map.put("[" ,  "%5B");
        map.put("\\" , "%5C");
        map.put("]" ,  "%5D");
        map.put("`" ,  "%60");
        map.put("^" ,  "%5E");
        map.put("_" ,  "%5F");
        
        for (String key : map.keySet()) {
            if (str.contains(key)) {
                System.out.println(str);
                str = str.replaceAll(key, map.get(key));
                System.out.println(str);
            }
        }

        return str;
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping("/itemGrade")
    @ResponseBody
    public String itemGrade() throws UnsupportedEncodingException {
        Document doc = null;
        Elements elements = null;
        JSONObject obj = new JSONObject();
        this.defaultUrl = "https://dunfaoff.com/Grade.df";
        
        try {
            doc = Jsoup.connect(defaultUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
            for (int i = 1; i < 4; i++) {
                elements = doc.select("#back > div > div.col-sm-12.col-md-12.col-xl-8.col-lg-8 > div:nth-child(1) > section > div > div:nth-child("+i+") > div > div.card-header > b");
                obj.put("src" + i, elements.select("img").attr("src"));
                obj.put("name" + i, elements.text());
                System.out.println(elements.text());
                elements = doc.select("#back > div > div.col-sm-12.col-md-12.col-xl-8.col-lg-8 > div:nth-child(1) > section > div > div:nth-child("+i+") > div > div.card-body > div:nth-child(1)");
                obj.put("grade" + i, elements.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        return obj.toJSONString();
    }
    
    // https://gall.dcinside.com/mgallery/board/lists?id=dnfqq&exception_mode=recommend
    @SuppressWarnings("unchecked")
    @RequestMapping("/dcinside")
    @ResponseBody
    public String dcinside(@RequestParam String path) throws UnsupportedEncodingException {
        Document doc = null;
        Elements elements = null;
        JSONObject obj = new JSONObject();
        
        if ("지마갤".equals(path)) {
            this.defaultUrl = "https://gall.dcinside.com/mgallery/board/lists?id=dnfqq&exception_mode=recommend";
        } else {
            this.defaultUrl = "https://gall.dcinside.com/board/lists?id=d_fighter_new1&exception_mode=recommend";
        }
        
        try {
            doc = Jsoup.connect(defaultUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
            if ("지마갤".equals(path)) {
                for (int i = 2; i < 7; i++) {
                    elements = doc.select("#container > section.left_content > article:nth-child(3) > div.gall_listwrap.list > table > tbody > tr:nth-child("+i+") > td.gall_tit.ub-word > a:nth-child(1)");
                    obj.put("title" + (i-1), elements.text());
                    obj.put("link" + (i-1), elements.attr("href"));
                    String sub = "";
                    elements = doc.select("#container > section.left_content > article:nth-child(3) > div.gall_listwrap.list > table > tbody > tr:nth-child("+i+") > td.gall_date");
                    sub += elements.text() + " | 조회수 - ";
                    elements = doc.select("#container > section.left_content > article:nth-child(3) > div.gall_listwrap.list > table > tbody > tr:nth-child("+i+") > td.gall_count");
                    sub += elements.text() + " | 추천 - ";
                    elements = doc.select("#container > section.left_content > article:nth-child(3) > div.gall_listwrap.list > table > tbody > tr:nth-child("+i+") > td.gall_recommend");
                    sub += elements.text();
                    obj.put("sub" + (i-1), sub);
                }
            } else {
                for (int i = 1; i < 6; i++) {
                    elements = doc.select("#container > section.left_content > article:nth-child(3) > div.gall_listwrap.list > table > tbody > tr:nth-child("+i+") > td.gall_tit.ub-word > a:nth-child(1)");
                    obj.put("title" + i, elements.text());
                    obj.put("link" + i, elements.attr("href"));
                    String sub = "";
                    elements = doc.select("#container > section.left_content > article:nth-child(3) > div.gall_listwrap.list > table > tbody > tr:nth-child("+i+") > td.gall_date");
                    sub += elements.text() + " | 조회수 - ";
                    elements = doc.select("#container > section.left_content > article:nth-child(3) > div.gall_listwrap.list > table > tbody > tr:nth-child("+i+") > td.gall_count");
                    sub += elements.text() + " | 추천 - ";
                    elements = doc.select("#container > section.left_content > article:nth-child(3) > div.gall_listwrap.list > table > tbody > tr:nth-child("+i+") > td.gall_recommend");
                    sub += elements.text();
                    obj.put("sub" + i, sub);
                }
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        return obj.toJSONString();
    }
}
