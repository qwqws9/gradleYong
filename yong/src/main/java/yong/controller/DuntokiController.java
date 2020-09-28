package yong.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import yong.dto.DuntokiDto;
import yong.service.DuntokiService;

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
public class DuntokiController extends BaseController {

    @Autowired
    private DuntokiService duntokiService;

    @RequestMapping("/teset/insert/{server}/{mainId}")
    @ResponseBody
    public String insert(@PathVariable String server, @PathVariable String mainId, @RequestParam String name) throws ParseException {
        System.out.println(server);
        System.out.println(mainId);
        System.out.println(name);
        
        DuntokiDto dto = new DuntokiDto();
        dto.setServerName(server);
        dto.setMainId(mainId);
        
        name = name.replaceAll("\\[", "");
        name = name.replaceAll("\\]", "");
        name = name.replaceAll("\"", "");
        
        String[] nameArr = name.split(",");
        
        List<String> list2 = new ArrayList<>();
        for (String temp : nameArr) {
            list2.add(temp);
        }
        
        
        List<DuntokiDto> list = this.duntokiService.duntokiList(new DuntokiDto(mainId));
        for (DuntokiDto dto2 : list) {
            boolean chk = Arrays.stream(nameArr).anyMatch(dto2.getSubId()::equals);
            if (chk) {
                list2.remove(dto2.getSubId());
            }
        }
        
        for (int i = 0; i < list2.size(); i++) {
            dto.setSubId(list2.get(i));
            this.duntokiService.duntokiSave(dto);
        }
        
        String returnMsg = "";
        for (int i = 0; i < list2.size(); i++) {
            returnMsg += list2.get(i);
            if (i != list2.size() - 1) {
                returnMsg += ",";
            }
        }
        
        if (nameArr.length != list2.size()) {
            return "중복된 데이터를 제외하고 " + returnMsg + " (" +list2.size() + ")건 추가했습니다.";
        }
        
        return "추가 완료 " + returnMsg;
    }
    
    @RequestMapping("/teset/select/{mainId}/{yn}")
    @ResponseBody
    public String select(@PathVariable String mainId, @PathVariable String yn) throws ParseException {
        StringBuilder sb = new StringBuilder();
        
        List<DuntokiDto> list = this.duntokiService.duntokiList(new DuntokiDto(mainId));
        if (list == null) {
            return mainId + "로 등록된 캐릭터가 존재하지 않습니다.";
        }
        
        boolean firstChk = true;
        for (DuntokiDto dto : list) {
            if (firstChk) {
                sb.append(dto.getMainId());
                sb.append(" - ");
                sb.append(dto.getServerName()); sb.append("*emrms*");
                sb.append("--------------------");
                firstChk = false;
            }
            sb.append(dto.getSeqno()); sb.append(" - ");
            sb.append(dto.getSubId()); sb.append("*emrms*");
        }
        
        if ("Y".equals(yn)) {
            sb.append("*emrms*");
            sb.append("삭제할 캐릭터명 앞에 숫자를"); sb.append("*emrms*");
            sb.append("/캐릭터삭제 {모험단명} {숫자}"); sb.append("*emrms*");
            sb.append("형식으로 입력해주세요.");
        }
        
        return sb.toString();
    }
    
    
    @RequestMapping("/teset/delete/{mainId}/{seqno}")
    @ResponseBody
    public String delete(@PathVariable String mainId, @PathVariable String seqno) throws ParseException {
        DuntokiDto dto = new DuntokiDto();
        dto.setMainId(mainId);
        if (!NumberUtils.isNumber(seqno)) {
            return "숫자를 입력해주세요.";
        }
        dto.setSeqno(Long.valueOf(seqno));
        
        DuntokiDto returnDto = this.duntokiService.duntokiDelete(dto);
        
        if (returnDto == null) {
            return mainId + "로 등록된 캐릭터가 존재하지 않습니다.";
        }
        
        return returnDto.getSubId() + " 삭제완료";
    }
    
    
    private String defaultUrl;
    private static final String DUNTOKI = "http://duntoki.xyz/";
    
    @RequestMapping("/duntoki/total/{mainId}")
    @ResponseBody
    public String duntoki(@PathVariable String mainId) throws UnsupportedEncodingException {
        
        StringBuilder sb = new StringBuilder();
        List<DuntokiDto> list = this.duntokiService.duntokiList(new DuntokiDto(mainId));
        if (list == null) {
            return "캐릭터 등록을 먼저 해주세요.";
        }
        
        String server = "";
        Document doc = null;
        String encName = "";
        
        try {
            int a = 0; // 신화 개수
            int b = 0; // 산물 개수
            int c = 0; // 시로코 골카 개수
            int d = 0; // 지혜인도 에픽 획득 수
            int e = 0; // 기타경로 에픽 획득 수
            
            for (DuntokiDto dto : list) {
                server = dto.getServerName();
                encName = encodeURIComponent(dto.getSubId());
                
                this.defaultUrl = DUNTOKI + "giraffe?serverNm="+server+"&charNm="+encName;
                doc = Jsoup.connect(defaultUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
                if (doc.text().indexOf("존재하지 않는 캐릭터") > -1) { continue; }
                
                String a1 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo2 > tbody > tr > td:nth-child(2)").text(); // 신화 갯수
                String a2 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo2 > tbody > tr > td:nth-child(3)").text(); // 산물
                String a3 =doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo3 > tbody > tr > td:nth-child(2)").text(); // 시로코 골카
                String a4 = doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo1 > tbody > tr > td:nth-child(2)").text(); // 획득 에픽 지혜인도
                String a5 = doc.select("body > center:nth-child(1) > div.informationDiv > table.getItemInfo1 > tbody > tr > td:nth-child(3)").text(); // 획득 에픽 기타경로
                
                a += Integer.parseInt(a1.split("개")[0]); // 신화 갯수
                b += Integer.parseInt(a2.split("개")[0]); // 산물
                c += Integer.parseInt(a3.split("개")[0]); // 시로코 골카
                d += Integer.parseInt(a4.split("개")[0]); // 획득 에픽 지혜인도
                e += Integer.parseInt(a5.split("개")[0]); // 획득 에픽 기타경로
            }
            
            
            sb.append("[" + server + " / " + mainId + "] *emrms*");
            sb.append("등록된 캐릭터 수 : " + list.size()); sb.append("*emrms*");
            sb.append("------------------------------"); sb.append("*emrms*");
            sb.append("지혜인도 근사값 : 약" + (d * 10) + "회"); sb.append("*emrms*");
            sb.append("획득한 산물 : " + b + "개"); sb.append("*emrms*");
            sb.append("시로코 골카 : " + c + "개"); sb.append("*emrms*");
            sb.append("획득 신화 : " + c + "개"); sb.append("*emrms*");
            sb.append("*emrms*");
            sb.append("-----------획득경로-----------"); sb.append("*emrms*");
            sb.append("지혜의 인도 : " + d + "개"); sb.append("*emrms*");
            sb.append("기타 경로 : " + e + "개"); sb.append("*emrms*");
            sb.append("------------------------------"); sb.append("*emrms*");
            
        } catch(Exception e) {
            return "조회 실패! *emrms* 관리자에게 문의주세요.";
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
}
