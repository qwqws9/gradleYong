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

import org.apache.commons.lang3.StringUtils;
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
    
    private static final String DUNDAM = "http://dundam.xyz/";
    private static final String DUNTOKI = "http://duntoki.xyz/";
    private static final String NEOPLE = "https://api.neople.co.kr/df/servers/";
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
            
            this.defaultUrl = DUNDAM + "searchActionTest.jsp?server="+engServer+"&name="+name;
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
            
            doc = Jsoup.connect(sb.toString()).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
            
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
                
                Elements AAA = doc.select("#rogen > table > tbody > tr"); // 로젠 1시
                for (Element ele : AAA) {
                    if (ele.select("th").text().indexOf("총 딜") == -1) { continue; }
                    rogen = ele.select("td:nth-child(3)").text();
                }
                
                AAA = doc.select("#sendbag > table > tbody > tr");
                for (Element ele : AAA) {
                    if (ele.select("th").text().indexOf("총 딜") == -1) { continue; }
                    send = ele.select("td:nth-child(2)").text();
                }
                
                AAA = doc.select("#siroco > table > tbody > tr");
                for (Element ele : AAA) {
                    if (ele.select("th").text().indexOf("총 딜") == -1) { continue; }
                    siroco = ele.select("td:nth-child(3)").text();
                }
                
                sb.setLength(0);
                sb.append(server + " / " + dename + "*emrms*");
                String[] infoArr = info.split("/");
                sb.append(infoArr[1] + " / " + infoArr[2].split(" ")[1] + "*emrms*");
                sb.append("------------------------------");
                sb.append("*emrms*");
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
            
            
            this.defaultUrl = DUNTOKI + "giraffe?serverNm="+server+"&charNm="+name;
            doc = Jsoup.connect(defaultUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
            if (doc.text().indexOf("존재하지 않는 캐릭터") > -1) { return "검색결과가 없습니다. *emrms* 아이디를 확인해주세요.";}
            
            sb.append("[" + server + " / " + name + "] *emrms*");
            sb.append("------------------------------"); sb.append("*emrms*");
            String a1 = doc.select("body > center:nth-child(1) > div:nth-child(2) > table:nth-child(6) > tbody > tr > td:nth-child(1) > font").text(); // 기린점수
            String a2 =doc.select("body > center:nth-child(1) > div:nth-child(2) > table:nth-child(6) > tbody > tr > td:nth-child(2) > font").text(); // 변동사항
            String a3 =doc.select("body > center:nth-child(1) > div:nth-child(2) > table:nth-child(12) > tbody > tr > td:nth-child(1) > font").text(); // 첫 신화
            String a4 =doc.select("body > center:nth-child(1) > div:nth-child(2) > table:nth-child(12) > tbody > tr > td:nth-child(2) > font").text(); // 신화 갯수
            String a5 =doc.select("body > center:nth-child(1) > div:nth-child(2) > table:nth-child(12) > tbody > tr > td:nth-child(3) > font").text(); // 산물
            String a6 =doc.select("body > center:nth-child(1) > div:nth-child(2) > table:nth-child(14) > tbody > tr > td:nth-child(3) > font").text(); // 잔향
            String a7 =doc.select("body > center:nth-child(1) > div:nth-child(2) > table:nth-child(14) > tbody > tr > td:nth-child(2) > font").text(); // 시로코 골카
            String a8 =doc.select("body > center:nth-child(1) > div:nth-child(2) > table:nth-child(14) > tbody > tr > td:nth-child(1) > font").text(); // 에픽도감 달성률
            String a9 =doc.select("body > center:nth-child(1) > div:nth-child(2) > table:nth-child(16) > tbody > tr > td:nth-child(1) > font").text(); // 던생 최고의날
            String a10 =doc.select("body > center:nth-child(1) > div:nth-child(2) > table:nth-child(16) > tbody > tr > td:nth-child(2) > font").text(); // 최고의날 획득 개수
            
            sb.append("기린점수 : " + a1); sb.append("*emrms*");
            sb.append(a2); sb.append("*emrms*");
            sb.append("첫 신화 : " + a3); sb.append("*emrms*");
            if (a4.indexOf("0") != -1) {
                sb.append("획득한 신화 : " + a4); sb.append("*emrms*");
            }
            sb.append("획득한 산물 : " + a5); sb.append("*emrms*");
            sb.append("잔향 획득여부 : " + a6); sb.append("*emrms*");
            sb.append("시로코 골카 : " + a7); sb.append("*emrms*");
            sb.append("던생 최고의 날 : " + a9); sb.append("*emrms*");
            sb.append("획득한 에픽 수 : " + a10); sb.append("*emrms*");
            sb.append("------------------------------"); sb.append("*emrms*");
            sb.append("에픽도감 달성률 : " + a8); sb.append("*emrms*");
            sb.append("------------------------------");
        } catch(Exception e) {
            return "조회 실패! *emrms* 관리자에게 문의주세요.";
        }
        
        return sb.toString();
    }
    

    @RequestMapping("/neople/equip/{server}")
    @ResponseBody
    public String neopleApi(@PathVariable String server, @RequestParam String name) throws UnsupportedEncodingException {
        
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
            
            URL url = new URL(NEOPLE + server +"/characters/"+ chcId +"/equip/equipment?apikey="+API_KEY);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            conn.setRequestMethod("GET");
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            
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
                        sb.append(enchantObj.get("value").toString()); // 마부 수치
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
            
            String a = NEOPLE + server + "/characters?characterName="+ name +"&limit=10&wordType=match&apikey="+ API_KEY;
            
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
}
