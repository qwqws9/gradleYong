package yong.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.collections4.CollectionUtils;
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
import yong.dto.LookDto;
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
public class NeopleApi extends BaseController {
    
    private static final String NEOPLE = "https://api.neople.co.kr/df/";
    private static final String API_KEY = "OLejPB3xs8EIqMVrvRNOrYp1eY3UD8oP";
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
    
    /**
     *  룩 조회
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/look/{server}/{characterId}")
    @ResponseBody
    public String a(@PathVariable String server, @PathVariable String characterId) {
        JSONObject resObj = new JSONObject();
        
        // 이미지
        resObj.put("img", "https://img-api.neople.co.kr/df/servers/" + server +"/characters/"+ characterId + "?zoom=1");
        
        List<LookDto> avatarList = this.getEquipAvatar(server, characterId);
        
        if (CollectionUtils.isEmpty(avatarList)) {
            resObj.put("msg", "아바타는 입히고 검색하셈;;");
            resObj.put("code", "400");
            return resObj.toJSONString();
        }
        
        List<LookDto> priceList = this.getPrice(avatarList);
        
        int totalPrice = 0;
        int totalAverage = 0;
        StringBuilder sb = new StringBuilder();
        DecimalFormat format = new DecimalFormat("###,###");
        
        for (LookDto l : priceList) {
            if (StringUtils.isEmpty(l.getUnitPrice())) {
                this.getSoldPrice(l);
            }
            
            // 시세 조회로 얻어온 경우
            if (l.isSoldItem()) {
                if (StringUtils.isEmpty(l.getAveragePrice())) {
                    l.setUnitPrice("교환 불가");
                    l.setAveragePrice("교환 불가");
                } else {
                    totalPrice += Integer.parseInt(l.getAveragePrice());
                    l.setUnitPrice("경매장에 없는 상품으로 최근판매금액 10개 평균값");
                }
            } else {
                totalPrice += Integer.parseInt(l.getUnitPrice());
                if (StringUtils.isEmpty(l.getAveragePrice())) {
                    l.setAveragePrice("평균 거래가 없음");
                } else {
                    totalAverage += Integer.parseInt(l.getAveragePrice());
                }
            }
            
            sb.append("▶");
            sb.append(l.getSlotName());
            sb.append("*emrms*");
            sb.append(l.getItemName());
            sb.append("*emrms*");
            if (StringUtils.isNotEmpty(l.getUnitPrice()) && l.getUnitPrice().startsWith("경매장 없는")) {
                sb.append(l.getUnitPrice());
            } else {
                sb.append("경매장 최저가 : ");
                sb.append(StringUtils.isNumeric(l.getUnitPrice()) ? format.format(Integer.parseInt(l.getUnitPrice())) + " 골드" : l.getUnitPrice());
            }
            sb.append("*emrms*");
            sb.append("경매장 평균가 : ");
            sb.append(StringUtils.isNumeric(l.getAveragePrice()) ? format.format(Integer.parseInt(l.getAveragePrice())) + " 골드" : l.getAveragePrice());
            sb.append("*emrms*");
            sb.append("------------------------------");
            
            
        }
        
        resObj.put("code", "200");
        resObj.put("data", sb.toString());
        resObj.put("totalPrice", "최저가 합계 : " + format.format(totalPrice) + " 골드");
        resObj.put("totalAverage", "평균가 합계 : " + format.format(totalAverage) + " 골드");
        
        return resObj.toJSONString();
    }

    private List<LookDto> getEquipAvatar(String server, String characterId) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        List<LookDto> list = new ArrayList<LookDto>();

        try {
            String a = NEOPLE + "servers/" + server + "/characters/" + characterId + "/equip/avatar?apikey=" + API_KEY;
            
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
            JSONArray jsonArr = (JSONArray) obj.get("avatar");
            LookDto look;
            for (Object o : jsonArr) {
                look = new LookDto();
                JSONObject j = (JSONObject) o;
                String slotId = (String) j.get("slotId");
                // 오라나 무기압타는 제외
                if ("AURORA".equals(slotId) || "WEAPON".equals(slotId)) { continue; }
                look.setSlotName((String) j.get("slotName"));
                look.setItemId((String) j.get("itemId"));
                look.setItemName((String) j.get("itemName"));

                JSONObject j2 = (JSONObject) j.get("clone");
                if (j2.get("itemId") != null) {
                    // 클론아바타 장착시 원본 아바타 추출
                    look.setItemId((String) j2.get("itemId"));
                    look.setItemName((String) j2.get("itemName"));
                }

                list.add(look);
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
        
        return list;
    }
    
    /**
     *  가격 조회
     * @return
     */
    private List<LookDto> getPrice(List<LookDto> itemList) {
        // https://api.neople.co.kr/df/auction?itemId=74746be97d76edb240b9f50cb4aca3c7&sort=unitPrice:asc&limit=1&apikey=OLejPB3xs8EIqMVrvRNOrYp1eY3UD8oP
        BufferedReader br = null;
        StringBuilder sb;
        
        try {
            for (LookDto l : itemList) {
                sb = new StringBuilder();
                String a = NEOPLE + "auction?itemId="+ l.getItemId() +"&sort=unitPrice:asc&limit=1&apikey=" + API_KEY;
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
                    l.setUnitPrice(((Long) obj.get("unitPrice")) + "");
                    l.setAveragePrice(((Long) obj.get("averagePrice")) + "");
                }
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
        
        return itemList;
    }
    
    /**
     *  시세 조회
     * @return
     */
    private LookDto getSoldPrice(LookDto item) {
        // https://api.neople.co.kr/df/auction?itemId=74746be97d76edb240b9f50cb4aca3c7&sort=unitPrice:asc&limit=1&apikey=OLejPB3xs8EIqMVrvRNOrYp1eY3UD8oP
        BufferedReader br = null;
        StringBuilder sb;
        
        try {
            sb = new StringBuilder();
            String a = NEOPLE + "auction-sold?itemId="+ item.getItemId() +"&limit=10&apikey=" + API_KEY;
            URL url = new URL(a);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            conn.setRequestMethod("GET");
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            Long price = 0L;
            int count = 0;
            JSONParser parse = new JSONParser();
            JSONObject obj = (JSONObject)parse.parse(sb.toString());
            JSONArray jsonArr = (JSONArray) obj.get("rows");
            item.setSoldItem(true);
            if(!obj.get("rows").toString().trim().equals("[]")) {
                for (Object o : jsonArr) {
                    count++;
                    JSONObject o2 = (JSONObject) o;
                    price += (Long) o2.get("unitPrice");
                }
                item.setAveragePrice(Math.round((price / count)) + "");
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
        
        return item;
    }
    /**
     *  
     * @return
     */
    public String b() {
        
        return "";
    }

}
